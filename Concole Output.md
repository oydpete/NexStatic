Started by user Oyedemi Pete

[Pipeline] Start of Pipeline
[Pipeline] node
Running on Jenkins
 in C:\data\jenkins_home\workspace\NexaSecTest
[Pipeline] {
[Pipeline] withEnv
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Clone Git Repository)
[Pipeline] git
The recommended git tool is: NONE
No credentials specified
Cloning the remote Git repository
Cloning repository https://github.com/juice-shop/juice-shop.git
 > git.exe init C:\data\jenkins_home\workspace\NexaSecTest # timeout=10
Fetching upstream changes from https://github.com/juice-shop/juice-shop.git
 > git.exe --version # timeout=10
 > git --version # 'git version 2.48.1.windows.1'
 > git.exe fetch --tags --force --progress -- https://github.com/juice-shop/juice-shop.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git.exe config remote.origin.url https://github.com/juice-shop/juice-shop.git # timeout=10
 > git.exe config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
Avoid second fetch
 > git.exe rev-parse "refs/remotes/origin/master^{commit}" # timeout=10
Checking out Revision bd1abe933411fee157548673165ca0d7c1b1e0d7 (refs/remotes/origin/master)
 > git.exe config core.sparsecheckout # timeout=10
 > git.exe checkout -f bd1abe933411fee157548673165ca0d7c1b1e0d7 # timeout=10
 > git.exe branch -a -v --no-abbrev # timeout=10
 > git.exe checkout -b master bd1abe933411fee157548673165ca0d7c1b1e0d7 # timeout=10
Commit message: "Fix indentation around emoji"
 > git.exe rev-list --no-walk bd1abe933411fee157548673165ca0d7c1b1e0d7 # timeout=10
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Gitleaks Scan)
[Pipeline] script
[Pipeline] {
[Pipeline] bat

C:\data\jenkins_home\workspace\NexaSecTest>docker run --rm -v "C:\data\jenkins_home\workspace\NexaSecTest:/app" -w /app zricethezav/gitleaks:v8.16.1                             detect --source . --report-format json --report-path gitleaks-report.json --exit-code 0 

    â—‹
    â”‚â•²
    â”‚ â—‹
    â—‹ â–‘
    â–‘    gitleaks

[90m8:15AM[0m [32mINF[0m 18923 commits scanned.
[90m8:15AM[0m [32mINF[0m scan completed in 5m0s
[90m8:15AM[0m [31mWRN[0m leaks found: 121
[Pipeline] archiveArtifacts
Archiving artifacts
Recording fingerprints
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Semgrep Scan)
[Pipeline] script
[Pipeline] {
[Pipeline] bat

C:\data\jenkins_home\workspace\NexaSecTest>docker run --rm -v "C:\data\jenkins_home\workspace\NexaSecTest:/src" returntocorp/semgrep:latest                             semgrep scan --config auto --json -o semgrep-report.json 
METRICS: Using configs from the Registry (like --config=p/ci) reports pseudonymous rule metrics to semgrep.dev.
To disable Registry rule metrics, use "--metrics=off".
Using configs only from local files (like --config=xyz.yml) does not enable metrics.

More information: https://semgrep.dev/docs/metrics

               
               
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”�
â”‚ Scan Status â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
  Scanning 1009 files tracked by git with 1061 Code rules:
                                                                                                                        
  Language      Rules   Files          Origin      Rules                                                                
 â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€        â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€                                                               
  <multilang>      77     673          Community    1061                                                                
  ts              166     468                                                                                           
  json              4     104                                                                                           
  yaml             31      85                                                                                           
  html              1      76                                                                                           
  solidity         21      17                                                                                           
  js              156      13                                                                                           
  dockerfile        5       1                                                                                           
  bash              4       1                                                                                           
                                                                                                                        
                
                
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”�
â”‚ Scan Summary â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
âœ… Scan completed successfully.
 â€¢ Findings: 109 (109 blocking)
 â€¢ Rules run: 305
 â€¢ Targets scanned: 1009
 â€¢ Parsed lines: ~99.9%
 â€¢ Scan skipped: 
   â—¦ Files larger than  files 1.0 MB: 8
   â—¦ Files matching .semgrepignore patterns: 139
 â€¢ Scan was limited to files tracked by git
 â€¢ For a detailed list of skipped files and lines, run semgrep with the --verbose flag
Ran 305 rules on 1009 files: 109 findings.

ðŸ“¢ Too many findings? Try Semgrep Pro for more powerful queries and less noise.
   See https://sg.run/false-positives.
[Pipeline] archiveArtifacts
Archiving artifacts
Recording fingerprints
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (NJSSCAN Scan)
[Pipeline] script
[Pipeline] {
[Pipeline] echo
Executing NJSSCAN: docker run --rm -v "C:\data\jenkins_home\workspace\NexaSecTest:/src" opensecurity/njsscan:latest njsscan /src --json -o /src/njsscan-report.json
[Pipeline] bat

C:\data\jenkins_home\workspace\NexaSecTest>docker run --rm -v "C:\data\jenkins_home\workspace\NexaSecTest:/src" opensecurity/njsscan:latest njsscan /src --json -o /src/njsscan-report.json 
[Pipeline] echo
NJSSCAN command finished with exit status: 1
[Pipeline] fileExists
[Pipeline] archiveArtifacts
Archiving artifacts
Recording fingerprints
[Pipeline] echo
NJSSCAN report found and archived.
[Pipeline] echo
NJSSCAN exited with status 1 (likely found vulnerabilities or had warnings). Marking build as UNSTABLE.
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Printing)
[Pipeline] script
[Pipeline] {
[Pipeline] echo
All Scans Have been attempt!!!
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Upload to DefectDojo)
[Pipeline] script
[Pipeline] {
[Pipeline] fileExists
[Pipeline] withCredentials
Masking supported pattern matches of %DD_APIKEY%
[Pipeline] {
[Pipeline] bat
Warning: A secret was passed to "bat" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [DD_APIKEY]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed

  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
100  125k    0     0  100  125k      0   102k  0:00:01  0:00:01 --:--:--  102k
100  125k    0     0  100  125k      0  57610  0:00:02  0:00:02 --:--:-- 57613
100  125k    0     0  100  125k      0  39591  0:00:03  0:00:03 --:--:-- 39583
100  125k    0     0  100  125k      0  30224  0:00:04  0:00:04 --:--:-- 30219
100  125k    0     0  100  125k      0  24457  0:00:05  0:00:05 --:--:-- 24498
100  125k    0     0  100  125k      0  20514  0:00:06  0:00:06 --:--:--     0
100  125k    0     0  100  125k      0  17671  0:00:07  0:00:07 --:--:--     0
100  125k    0     0  100  125k      0  15506  0:00:08  0:00:08 --:--:--     0
100  126k  100  1271  100  125k    137  13948  0:00:09  0:00:09 --:--:--   255
100  126k  100  1271  100  125k    137  13947  0:00:09  0:00:09 --:--:--   320
[Pipeline] echo
Warning: A secret was passed to "echo" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [DD_APIKEY]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
DefectDojo response for Gitleaks: 
C:\data\jenkins_home\workspace\NexaSecTest>curl -X POST "http://localhost:8081/api/v2/import-scan/"             -H "Authorization: Token ****"             -H "accept: application/json"             -H "Content-Type: multipart/form-data"             -F "file=@\"gitleaks-report.json\""             -F "scan_type=Gitleaks Scan"             -F "engagement=1"             -F "verified=true"             -F "active=true"             -F "minimum_severity=Low" 
{"minimum_severity":"Low","active":true,"verified":true,"endpoint_to_add":null,"auto_create_context":false,"deduplication_on_engagement":false,"lead":null,"push_to_jira":false,"api_scan_configuration":null,"create_finding_groups_for_all_findings":true,"test_id":28,"engagement_id":1,"product_id":2,"product_type_id":1,"statistics":{"after":{"info":{"active":0,"verified":0,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":0},"low":{"active":0,"verified":0,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":0},"medium":{"active":0,"verified":0,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":0},"high":{"active":112,"verified":112,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":112},"critical":{"active":0,"verified":0,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":0},"total":{"active":112,"verified":112,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":112}}},"apply_tags_to_findings":false,"apply_tags_to_endpoints":false,"scan_type":"Gitleaks Scan","engagement":1,"close_old_findings":false,"close_old_findings_product_scope":false,"test":28}
[Pipeline] }
[Pipeline] // withCredentials
[Pipeline] fileExists
[Pipeline] withCredentials
Masking supported pattern matches of %DD_APIKEY%
[Pipeline] {
[Pipeline] bat
Warning: A secret was passed to "bat" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [DD_APIKEY]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed

  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
100  219k    0     0  100  219k      0  1003k --:--:-- --:--:-- --:--:--  999k
100  219k    0     0  100  219k      0   179k  0:00:01  0:00:01 --:--:--  179k
100  219k    0     0  100  219k      0    98k  0:00:02  0:00:02 --:--:--   98k
100  219k    0     0  100  219k      0  69551  0:00:03  0:00:03 --:--:-- 69534
100  219k    0     0  100  219k      0  52942  0:00:04  0:00:04 --:--:-- 52940
100  219k    0     0  100  219k      0  42841  0:00:05  0:00:05 --:--:--     0
100  219k    0     0  100  219k      0  35938  0:00:06  0:00:06 --:--:--     0
100  219k    0     0  100  219k      0  30933  0:00:07  0:00:07 --:--:--     0
100  219k    0     0  100  219k      0  27175  0:00:08  0:00:08 --:--:--     0
100  219k    0     0  100  219k      0  24213  0:00:09  0:00:09 --:--:--     0
100  221k  100  1277  100  219k    130  23049  0:00:09  0:00:09 --:--:--   283
[Pipeline] echo
Warning: A secret was passed to "echo" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [DD_APIKEY]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
DefectDojo response for Semgrep: 
C:\data\jenkins_home\workspace\NexaSecTest>curl -X POST "http://localhost:8081/api/v2/import-scan/"             -H "Authorization: Token ****"             -H "accept: application/json"             -H "Content-Type: multipart/form-data"             -F "file=@\"semgrep-report.json\""             -F "scan_type=Semgrep JSON Report"             -F "engagement=1"             -F "verified=true"             -F "active=true"             -F "minimum_severity=Low" 
{"minimum_severity":"Low","active":true,"verified":true,"endpoint_to_add":null,"auto_create_context":false,"deduplication_on_engagement":false,"lead":null,"push_to_jira":false,"api_scan_configuration":null,"create_finding_groups_for_all_findings":true,"test_id":29,"engagement_id":1,"product_id":2,"product_type_id":1,"statistics":{"after":{"info":{"active":0,"verified":0,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":0},"low":{"active":2,"verified":2,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":2},"medium":{"active":26,"verified":26,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":26},"high":{"active":80,"verified":80,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":80},"critical":{"active":0,"verified":0,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":0},"total":{"active":108,"verified":108,"duplicate":0,"false_p":0,"out_of_scope":0,"is_mitigated":0,"risk_accepted":0,"total":108}}},"apply_tags_to_findings":false,"apply_tags_to_endpoints":false,"scan_type":"Semgrep JSON Report","engagement":1,"close_old_findings":false,"close_old_findings_product_scope":false,"test":29}
[Pipeline] }
[Pipeline] // withCredentials
[Pipeline] fileExists
[Pipeline] withCredentials
Masking supported pattern matches of %DD_APIKEY%
[Pipeline] {
[Pipeline] bat
Warning: A secret was passed to "bat" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [DD_APIKEY]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed

  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
100   98k  100   171  100   98k   1237   712k --:--:-- --:--:-- --:--:--  714k
[Pipeline] echo
Warning: A secret was passed to "echo" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [DD_APIKEY]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
DefectDojo response for NJSSCAN: 
C:\data\jenkins_home\workspace\NexaSecTest>curl -X POST "http://localhost:8081/api/v2/import-scan/"             -H "Authorization: Token ****"             -H "accept: application/json"             -H "Content-Type: multipart/form-data"             -F "file=@\"njsscan-report.json\""             -F "scan_type=NJSSCAN Scan"             -F "engagement=1"             -F "verified=true"             -F "active=true"             -F "minimum_severity=Low" 
{"scan_type":["\"NJSSCAN Scan\" is not a valid choice."],"message":"{'scan_type': [ErrorDetail(string='\"NJSSCAN Scan\" is not a valid choice.', code='invalid_choice')]}"}
[Pipeline] }
[Pipeline] // withCredentials
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Declarative: Post Actions)
[Pipeline] cleanWs
[WS-CLEANUP] Deleting project workspace...
[WS-CLEANUP] Deferred wipeout is used...
[WS-CLEANUP] done
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: UNSTABLE
