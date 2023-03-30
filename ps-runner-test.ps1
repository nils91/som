# Count number of scripts failed, success and total
$success = 0
$failed = 0
$total = 0

# Traverse the /test directory and execute all .ps1 scripts
Get-ChildItem -Path test -Recurse -Include *.ps1 | ForEach-Object {
    if($_.FullName -notlike "*\fixtures\*" -and $_.FullName -notlike "*\tmp\*") {
        $dir = Split-Path $_.FullName
        Set-Location $dir
        Write-Host "--------------------------"
        Write-Host "Running $($_.FullName)"
        Write-Host "--------------------------"
        $output = & $_.FullName
        Write-Host $output
        Write-Host "--------------------------"
        if ($LASTEXITCODE -eq 0) {
            Write-Host "$($_.FullName) succeeded"
            $success++
        } else {
            Write-Host "$($_.FullName) failed"
            $failed++
        }
        $total++
        Set-Location -Path $env:USERPROFILE
        Write-Host "--------------------------"
    }
}

Write-Host "$success/$total successful"
if ($failed -gt 0) {
    Write-Host "Not successful"
    exit 1
}
Write-Host "Successful"
