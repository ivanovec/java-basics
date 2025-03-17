import jetbrains.buildServer.configs.kotlin.*

version = "2024.12"

project{
    buildType {
        id("JavaBasics")
        name = "Java Basics"
        description = "Java Basics Course"

        steps {
            script  {
                name = "Add runner.sh"
                id = "YAML"
                scriptContent = """
            /bin/cat <<'EOM' >runner.sh
            #!/bin/bash
            cp /data/project/ui.sarif.json /data/results/qodana.sarif.json 
            EOM
            chmod +x runner.sh
        """.trimIndent()
            }

            qodana {
                name = "Run fake Qodana analysis"
                linter = customLinter {
                    image = "jetbrains/qodana-jvm:2023.3"
                }
                additionalDockerArguments = """
                --entrypoint="/data/project/runner.sh"
            """.trimIndent()

                reportAsTests = true
                param("report-as-test-mode", "each-inspection-is-test")

            }
        }

        vcs {
            root(JavaBasics_HttpsGithubComIvanovecJavaBasicsGitRefsHeadsMaster)
        }
    }
}