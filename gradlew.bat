@rem ****************************************************************************
@rem 
@rem  Gradle startup script for Windows
@rem
@rem This script sets the necessary environment variables for running Gradle
@rem and adds Gradle distribution to the PATH.
@rem 
@rem ****************************************************************************

@rem Set Gradle user home
set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"

@rem Add Gradle distribution to PATH
set "PATH=%GRADLE_USER_HOME%\gradle\gradle-6.8.3\bin;%PATH%"

@rem Verify that Gradle is installed correctly
gradle -v
