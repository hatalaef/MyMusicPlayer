import sys
import glob
import datetime

def getTheseFiles(directory, fileType):
    files = glob.glob(directory + "/*" + fileType)
    
    return files

def saveCurrentTime(directory, fileName):
    file = open(directory + "\\" + fileName, "w")
    timeStamp = datetime.datetime.now()
    file.write(str(timeStamp))
    file.close()

def getLastTime(directory, fileName):
    file = open(directory + "\\" + fileName, "r")
    theTime = file.read()
    file.close()
    
    return theTime