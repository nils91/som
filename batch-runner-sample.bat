REM Count number of scripts failed, success, and total
set success=0
set failed=0
set total=0

REM Traverse the /test directory and execute all .bat scripts
for /r "sample" %%f in (*.bat) do (
    set "dir=%%~dpf"
    cd /d "!dir!"
    echo Running %%f
    if %%f (
        echo %%f succeeded
        set /a "success+=1"
    ) else (
        echo %%f failed
        set /a "failed+=1"
    )
    set /a "total+=1"
    cd /d "-"
)

echo %success%/%total% successful
if %failed% gtr 0 (
    echo Not successful
    exit /b 1
)

echo Successful
