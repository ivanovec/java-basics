import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.Qodana
import jetbrains.buildServer.configs.kotlin.buildSteps.qodana
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.12"

project {

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = """
                /bin/cat <<'EOM' >runner.sh
                            #!/bin/bash
                            cp /data/project/ui.sarif.json /data/results/qodana.sarif.json 
                            EOM
                            chmod +x runner.sh
            """.trimIndent()
        }
        qodana {
            name = "qodana"
            linter = jvm {
                version = Qodana.JVMVersion.LATEST
            }
            inspectionProfile = default()
            additionalDockerArguments = """--entrypoint="/data/project/runner.sh""""
            cloudToken = "credentialsJSON:84879d2a-f4fa-4bb1-8040-48d6419f8e1b"
            param("code-inspection-profile-name", "qodana.recommended")
            param("namesAndTagsCustom", "jetbrains/qodana")
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})
