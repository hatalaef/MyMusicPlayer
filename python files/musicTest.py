import eyed3
import sys
import pdb

import musicDatabase
import getFiles

checkFile = False

db = "music.db"
musicDirectory = "C:\Users\Emily\AndroidStudioProjects\MyMusicPlayer\python files\musicFiles"
playlistDirectory = "C:\Users\Emily\AndroidStudioProjects\MyMusicPlayer\python files\playlist"
fileDirectory = "C:\Users\Emily\AndroidStudioProjects\MyMusicPlayer\python files"
file = "lastTime.txt"

getFiles.saveCurrentTime(fileDirectory, file)

newFilePaths = musicDatabase.updateSinceLastRun(db, fileDirectory, file)

musicFiles = getFiles.getTheseFiles(musicDirectory, ".mp3")
playlistFiles = getFiles.getTheseFiles(playlistDirectory, ".m3u")


lastFile = musicDirectory + "\\" + musicDatabase.getLastSongRow(db)

for file in musicFiles:
    musicDatabase.compareTimes(db, file)
    if checkFile and file == lastFile:
        checkFile = False
    elif not checkFile:
        musicDatabase.addSongToDb(db, file)


