//package simple.automation;
//
//import com.sun.source.tree.*;
//import com.sun.source.util.*;
//import javax.tools.*;
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//public class AstTreePrinter {
//
//    public static void main(String[] args) throws IOException {
//        String filePath = "src/../rep/Example.java";
//
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
//        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(new File(filePath));
//        JavacTask task = (JavacTask) compiler.getTask(null, fileManager, null, null, null, files);
//        Iterable<? extends CompilationUnitTree> compilationUnits = task.parse();
//
//        for (CompilationUnitTree cu: compilationUnits) {
//            System.out.println("AST for: " + cu.getSourceFile().getName() );
//            new PrettyTreePrinter().scan(cu, "", true);
//        }
//    }
//
//    static class PrettyTreePrinter extends TreeScanner<Void, PrettyTreePrinter.TreePrintContext>{
//        public void scan(Tree tree, String prefix, boolean isLast) {
//            super.scan(tree, new TreePrintContext(prefix, isLast));
//        }
//
//        @Override
//        public Void scan(Tree tree, TreePrintContext context) {
//            if(tree == null) return null;
//
//            String branch = context.isLast ? "└──" : "├──";
//            System.out.print(context.prefix);
//            System.out.print(branch);
//            System.out.println(tree.getKind() + ":" + summarize(tree));
//
//            String childPrefix = context.prefix + (context.isLast?"    ":"|   ");
//            return super.scan(tree, new TreePrintContext(childPrefix, false));
//        }
//
//        @Override
//        public Void visitClass(ClassTree node, TreePrintContext context) {
//            scanNodeWithChildren(node, context, node.getMembers());
//            return null;
//        }
//
//        @Override
//        public Void visitMethod(MethodTree node, TreePrintContext context) {
//            scanNodeWithChildren(node, context, node.getBody() != null ? node.getBody().getStatements() : List.of());
//            return null;
//        }
//
//        @Override
//        public Void visitBlock(BlockTree node, TreePrintContext context) {
//            scanNodeWithChildren(node, context, node.getStatements());
//            return null;
//        }
//
//        @Override
//        public Void visitCompilationUnit(CompilationUnitTree node, TreePrintContext context) {
//            scanNodeWithChildren(node, context, node.getTypeDecls());
//            return null;
//        }
//
//        private void scanNodeWithChildren(Tree node, TreePrintContext context, List<? extends Tree> children){
//            String childPrefix = context.prefix + (context.isLast ? "    " : "│   ");
//
//            for (int i = 0; i < children.size(); i++) {
//                Tree child = children.get(i);
//                boolean last = (i == children.size() - 1);
//                scan(child, new TreePrintContext(childPrefix, last));
//            }
//        }
//
//        private String summarize(Tree tree) {
//            try {
//                return switch (tree.getKind()) {
//                    case CLASS -> ((ClassTree)tree).getSimpleName().toString();
//                    case METHOD -> ((MethodTree)tree).getName().toString();
//                    case VARIABLE -> ((VariableTree)tree).getName().toString();
//                    default -> tree.toString().split("\n")[0];
//                };
//            } catch (Exception e) {
//                return "";
//            }
//        }
//
//        record TreePrintContext(String prefix, boolean isLast){}
//    }
//}
