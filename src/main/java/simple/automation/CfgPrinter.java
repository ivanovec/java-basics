package simple.automation;

import com.sun.source.tree.*;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CfgPrinter {

    public static void main(String[] args) throws IOException {
        String filePath = "src/../rep/Example.java";

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(new File(filePath));
        JavacTask task = (JavacTask) compiler.getTask(null, fileManager, null, null, null, files);
        Iterable<? extends CompilationUnitTree> compilationUnits = task.parse();

        for (CompilationUnitTree cu : compilationUnits) {
            System.out.println("CFG for: " + cu.getSourceFile().getName());
            new CFGBuilder().scan(cu, null);
        }
    }

    static class CFGBuilder extends TreeScanner<Void, Void> {
        @Override
        public Void visitMethod(MethodTree methodTree, Void unused) {
            System.out.println("Method: " + methodTree.getName());
            if (methodTree.getBody() != null) {
                CFG cfg = new CFG();
                cfg.build(methodTree.getBody().getStatements());
                cfg.printAscii();
            }
            return null;
        }
    }

    static class CFG {

        private final List<BasicBlock> blocks = new ArrayList<>();
        private final Map<BasicBlock, List<BasicBlock>> edges = new LinkedHashMap<>();
        private int blockCounter = 0;

        public void build(List<? extends StatementTree> statements) {
            BasicBlock entry = newBlock();
            BasicBlock exit = newBlock();
            buildFromStatements(statements, entry, exit);
        }

        private void buildFromStatements(List<? extends StatementTree> statements, BasicBlock current, BasicBlock exit) {
            for (StatementTree stmt : statements) {
                if (stmt instanceof IfTree ifTree) {
                    BasicBlock conditionBlock = newBlock();
                    conditionBlock.addStatement(ifTree);
                    connect(current, conditionBlock);

                    BasicBlock thenBlock = newBlock();
                    BasicBlock elseBlock = newBlock();
                    BasicBlock afterIf = newBlock();

                    ifTree.getThenStatement().accept(new SubStatementScanner(), thenBlock);
                    if (!thenBlock.statements.isEmpty()) connect(conditionBlock, thenBlock);
                    connect(thenBlock, afterIf);

                    if (ifTree.getElseStatement() != null) {
                        ifTree.getElseStatement().accept(new SubStatementScanner(), elseBlock);
                        if (!elseBlock.statements.isEmpty()) connect(conditionBlock, elseBlock);
                        connect(elseBlock, afterIf);
                    } else {
                        connect(conditionBlock, afterIf);
                    }

                    blocks.add(conditionBlock);
                    blocks.add(thenBlock);
                    if (!elseBlock.statements.isEmpty()) blocks.add(elseBlock);
                    blocks.add(afterIf);

                    current = afterIf;

                } else {
                    current.addStatement(stmt);
                }
            }
            if (!blocks.contains(current)) blocks.add(current);
        }

        public void printAscii() {
            Map<BasicBlock, String> blockLabels = new HashMap<>();
            for (BasicBlock block : blocks) {
                blockLabels.put(block, "B" + block.id);
            }

            for (BasicBlock block : blocks) {
                String label = blockLabels.get(block);
                System.out.println(label + ":");
                System.out.println(block.asAscii());

                List<BasicBlock> successors = edges.getOrDefault(block, Collections.emptyList());
                for (BasicBlock succ : successors) {
                    System.out.println("    → " + blockLabels.get(succ));
                }
                if (!successors.isEmpty()) System.out.println();
            }
        }

        private BasicBlock newBlock() {
            BasicBlock b = new BasicBlock(blockCounter++);
            return b;
        }

        private void connect(BasicBlock from, BasicBlock to) {
            edges.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        static class SubStatementScanner extends TreeScanner<Void, BasicBlock> {
            @Override
            public Void visitBlock(BlockTree blockTree, BasicBlock b) {
                for (StatementTree stmt : blockTree.getStatements()) {
                    b.addStatement(stmt);
                }
                return null;
            }

            @Override
            public Void visitExpressionStatement(ExpressionStatementTree tree, BasicBlock b) {
                b.addStatement(tree);
                return null;
            }

            @Override
            public Void visitVariable(VariableTree tree, BasicBlock b) {
                b.addStatement(tree);
                return null;
            }

            @Override
            public Void visitReturn(ReturnTree returnTree, BasicBlock b) {
                b.addStatement(returnTree);
                return null;
            }
        }
    }

    static class BasicBlock {
        final int id;
        final List<StatementTree> statements = new ArrayList<>();

        BasicBlock(int id) {
            this.id = id;
        }

        void addStatement(StatementTree stmt) {
            statements.add(stmt);
        }

        String asAscii() {
            StringBuilder sb = new StringBuilder();
            sb.append("  ┌───────────────────────┐\n");
            if (statements.isEmpty()) {
                sb.append("  │       (empty)        │\n");
            } else {
                for (StatementTree stmt : statements) {
                    String line = stmt.toString().replaceAll("\n", " ").trim();
                    if (line.length() > 23) line = line.substring(0, 20) + "...";
                    sb.append("  │ ").append(String.format("%-21s", line)).append("│\n");
                }
            }
            sb.append("  └───────────────────────┘");
            return sb.toString();
        }
    }
}
