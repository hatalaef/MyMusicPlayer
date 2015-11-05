import sqlite3 as lite
import sys
import warnings
import eyed3
import os
import datetime
import time
import math
import getFiles
import pdb

reload(sys)
sys.setdefaultencoding("Cp1252")

eyed3.log.setLevel("ERROR")

FOLDER = "musicFiles"

db = None

def addSongToDb(dbName, filePath):


    audioFile = eyed3.load(filePath)

    global db
    
    title = ""
    artist = ""
    album = ""
    genre = ""
    path = filePath.split(FOLDER + "\\")[1]
    
    parameters = ""
    values = ""
    
    
    if audioFile.tag.title is not None:
        title = audioFile.tag.title
    if audioFile.tag.artist is not None:
        artist = audioFile.tag.artist    
    if audioFile.tag.album is not None:
        album = audioFile.tag.album
    if audioFile.tag.genre is not None and audioFile.tag.genre.name is not None:
        genre = audioFile.tag.genre.name
    
    try:
        db = lite.connect(dbName)
        cursor = db.cursor()
        cursor.execute("INSERT INTO Songs (title, artist, album, genre, filePath) VALUES (?, ?, ?, ?, ?)", (title, artist, album, genre, path))
        db.commit()
        print("Added: " + filePath)
        print
    except lite.IntegrityError, e:
            print "Rolling back: %s" % e.args[0]
            db.rollback()
            return    
    except lite.Error, e:
        print "Error: %s" %e.args[0]
        print sys.exc_traceback.tb_lineno
        raise e     
    finally:
        db.close()
        
def findSongByTitle(dbName, title):
            
    global db
    
    try:
        db = lite.connect(dbName)
        cursor = db.cursor()
        results = cursor.execute("SELECT * FROM Songs WHERE title = ?", (title,))
        id = results.fetchone()
        
        db.commit()
        
    except lite.Error, e:
        print "Error: %s" %e.args[0]
        print sys.exc_traceback.tb_lineno
        raise e
    finally:
        db.close()
        
    return id    
        
def updateSong(dbName, id, title = "", artist = "", album = "", genre = "", filePath = ""):
    global db
    
    if filePath.contains(FOLDER):
        filePath = filePath.split(FOLDER + "/")[1]
    
    sqlStr = "UPDATE Songs SET title = %s, artist = %s, album = %s, genre = %s, filePath = %s,WHERE id = %d" % (title, artist, album, genre, filePath, id)
    print sqlStr
    
    if values != "":    
        try:
            db = lite.connect(dbName)
            cursor = db.cursor()
            cursor.execute("UPDATE Songs SET title = ?, artist = ?, album = ?, genre = ?, filePath = ? WHERE id = ?", (title, artist, album, genre, filePath, id))
            
            db.commit()
        
        except lite.Error, e:
            print "Error: %s" %e.args[0]
            print sys.exc_traceback.tb_lineno
            raise e
        finally:
            db.close()
            
def getLastSongRow(dbName):
    global db
    
    filePath = ""

    try:
        db = lite.connect(dbName)
        cursor = db.cursor()
        
        fileExists = cursor.execute("SELECT EXISTS(SELECT 1 FROM Songs ORDER BY id DESC LIMIT 1)").fetchone()
        if fileExists[0] == 1:
            filePath = cursor.execute("SELECT filePath FROM Songs ORDER BY id DESC LIMIT 1").fetchone()[0]
        
    finally:
        db.close()
    
    return filePath
    
def compareTimes(dbName, filePath):
    global db
    
    try:
        db = lite.connect(dbName)
        cursor = db.cursor()
        fileExists = cursor.execute("SELECT EXISTS(SELECT 1 FROM Songs WHERE filePath = ?)", (filePath.decode("Cp1252"),)).fetchone()
        if fileExists[0] == 1:
            timeStamp = cursor.execute("SELECT timeStamp from Songs WHERE filePath = ?", (filePath.decode("Cp1252"),)).fetchone()[0]
            
            timeStampSql = datetime.datetime.strptime(timeStamp, "%Y-%m-%d %H:%M:%S")
            
            fileTime = math.ceil(os.stat(filePath).st_mtime)
            timeStampFile = datetime.datetime.fromtimestamp(fileTime)
            
            return timeStamp
    
    finally:
        db.close()
        
    
        
def updateSinceLastRun(dbName, directory, fileName):
    global db
    
    lastTimeStr = getFiles.getLastTime(directory, fileName).split(".")[0]
    lastTime = datetime.datetime.strptime(lastTimeStr, "%Y-%m-%d %H:%M:%S")
    
    try:
        db = lite.connect(dbName)
        cursor = db.cursor()
        filePaths = cursor.execute("SELECT filePath FROM Songs WHERE timeStamp > ?", (lastTime,)).fetchall()
        
        return filePaths
        
    except lite.Error, e:
            print "Error: %s" %e.args[0]
            print sys.exc_traceback.tb_lineno
            raise e    
    finally:
        db.close()