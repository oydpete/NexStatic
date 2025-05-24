//The Function to Upload to DefectDojo Using the Rest API

def Upload(Map params) {
    withCredentials([string(credentialsId: 'DefectDojoApi', variable: 'DD_APIKEY')]) {
        def uploadCmd = """
            curl -X POST "${DEFECTDOJO_URL}/api/v2/import-scan/" ^
            -H "Authorization: Token ${DD_APIKEY}" ^
            -H "accept: application/json" ^
            -H "Content-Type: multipart/form-data" ^
            -F "file=@\\"${params.reportFile}\\"" ^
            -F "scan_type=${params.scanType}" ^
            -F "engagement=${ENGAGEMENT_ID}" ^
            -F "verified=true" ^
            -F "active=true" ^
            -F "minimum_severity=Low"
        """
        def response = bat(script: uploadCmd, returnStdout: true)
        echo "DefectDojo response for ${params.tool}: ${response}"
    }
}

pipeline {
    agent any
     

     //Declearing Environmental Variables
    environment {
        GITLEAKS_VERSION = 'v8.16.1'
        SEMGREP_VERSION = 'latest'
        NJSSCAN_VERSION = 'latest' 
        DEFECTDOJO_URL = 'http://localhost:8081'
        ENGAGEMENT_ID = '1'
    }
      

    // Pipeline Stages
    stages {


        stage('Clone Git Repository') {

            steps {
                git branch: 'master', url: 'https://github.com/juice-shop/juice-shop.git'
            }
        }

        stage('Gitleaks Scan') {

            steps {

                script {
                    try {
                        
                        bat """
                            docker run --rm -v "${WORKSPACE}:/app" -w /app zricethezav/gitleaks:${GITLEAKS_VERSION} ^
                            detect --source . --report-format json --report-path gitleaks-report.json --exit-code 0
                        """
                        archiveArtifacts artifacts: 'gitleaks-report.json', fingerprint: true
                        
                    } 
                    catch (err) {
                        echo "Gitleaks Scan Failed: ${err}"
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }

        stage('Semgrep Scan') {

            steps {

                script {
                    try {
                        bat """
                            docker run --rm -v "${WORKSPACE}:/src" returntocorp/semgrep:${SEMGREP_VERSION} ^
                            semgrep scan --config auto --json -o semgrep-report.json
                        """
                        archiveArtifacts artifacts: 'semgrep-report.json', fingerprint: true

                    } 
                    
                    catch (err) {
                        echo "Semgrep Scan Failed: ${err}"
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }

        stage('NJSSCAN Scan') {

            steps {

                script {
                    try {
                        def njsscanCommand = """docker run --rm -v "${WORKSPACE}:/src" opensecurity/njsscan:${NJSSCAN_VERSION} njsscan /src --json -o /src/njsscan-report.json"""

                        echo "Executing NJSSCAN: ${njsscanCommand}"
                        def exitStatus = bat(script: njsscanCommand, returnStatus: true)
                        echo "NJSSCAN command finished with exit status: ${exitStatus}"

                        // Check if the report file was generated
                        if (fileExists('njsscan-report.json')) {
                            archiveArtifacts artifacts: 'njsscan-report.json', fingerprint: true
                            echo "NJSSCAN report found and archived."
                            // If njsscan exited non-zero (e.g., found vulnerabilities), but the report exists, mark as UNSTABLE.
                            if (exitStatus != 0) {
                                echo "NJSSCAN exited with status ${exitStatus} (likely found vulnerabilities or had warnings). Marking build as UNSTABLE."
                                currentBuild.result = 'UNSTABLE'
                            }
                        } else {
                            // If the report file is NOT found, this is a critical failure of the tool.
                            echo "ERROR: NJSSCAN report 'njsscan-report.json' was NOT generated. NJSSCAN process exit status: ${exitStatus}."
                            currentBuild.result = 'FAILURE'
                            // Explicitly fail the pipeline stage
                            error("NJSSCAN failed to produce a report. Exit status from njsscan command: ${exitStatus}")
                        }
                    } catch (e) {
                        // Catch any other exceptions during the script block
                        echo "An error occurred in the NJSSCAN stage: ${e.message}"
                        currentBuild.result = 'FAILURE'
                        // If error() was already called, this specific error message might be more general.
                        // Ensure the pipeline reflects the failure.
                        if (!(e instanceof hudson.AbortException && e.message.contains("NJSSCAN failed to produce a report"))) {
                            error("Unhandled exception in NJSSCAN stage: ${e.message}")
                        }
                    }
                }
            }
        }
        stage('Printing') {
            steps{

                script{

                    echo "All Scans Have been attempt!!!"
                }
            }
        }

        stage('Upload to DefectDojo') {

            steps {
                script {
                      // Ensuring the whole Pipeline doesnt fail if some Scan was Unsuccesful(making code more reselient)


                    // Check for Gitleaks Scan
                    if (fileExists('gitleaks-report.json')) {
                        try {
                            Upload(tool: 'Gitleaks', reportFile: 'gitleaks-report.json', scanType: 'Gitleaks Scan')
                        } catch (err) {
                            echo "DefectDojo upload for Gitleaks failed: ${err}"
                            currentBuild.result = 'FAILURE' 
                        }
                    } 
                    else {
                        echo "Skipped Gitleaks Scan upload to DefectDojo: report file not found."
                    }



                    // Check for Semgrep Scan
                    if (fileExists('semgrep-report.json')) {
                        try {

                            Upload(tool: 'Semgrep', reportFile: 'semgrep-report.json', scanType: 'Semgrep JSON Report')
                        } catch (err) {

                            echo "DefectDojo upload for Semgrep failed: ${err}"
                            currentBuild.result = 'FAILURE'
                        }
                    } else {
                        echo "Skipped Semgrep Scan Upload to DefectDojo: report file not found."
                    }



                    // Check for NJSSCAN Scan
                    if (currentBuild.result != 'FAILURE' && fileExists('njsscan-report.json')) {   //Check if NJSSCAN was successful and File Exist
                        try {
                            Upload(tool: 'NJSSCAN', reportFile: 'njsscan-report.json', scanType: 'NJSSCAN Scan')

                        } 
                        
                        catch (err) {

                            echo "DefectDojo upload for NJSSCAN failed: ${err}"
                            currentBuild.result = 'FAILURE'
                        }
                    }
                    
                     else if (currentBuild.result != 'FAILURE') { // Check if scan was successful but file not found

                        echo "Skipped NJSSCAN Scan Upload to DefectDojo: report file 'njsscan-report.json' not found."
                        // If the NJSSCAN stage was UNSTABLE but produced a report, it would attempt upload.
                        // If the NJSSCAN stage FAILED (e.g. report not produced), this block might not be strictly needed
                        // as the pipeline is already failing.
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
