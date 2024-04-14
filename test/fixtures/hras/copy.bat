@echo off
setlocal enabledelayedexpansion

set "filename=minimal_return1.hras"
set "suffixes=decimal_explicit decimal_base_explicit bin oct hexx hexh decimal"

for %%s in (%suffixes%) do (
    set /a count+=1
    for %%f in ("%filename%") do (
        copy "%%f" "%%~nf_%%s%%~xf" > nul
    )
)

echo Copies created successfully.