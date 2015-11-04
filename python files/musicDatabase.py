import sqlite3 as lite
import sys
import warnings
import eyed3
import os
import datetime
import time
import math
import getFiles

reload(sys)
sys.setdefaultencoding("Cp1252")

eyed3.log.setLevel("ERROR")

db = None

def addSongToDb(dbName, filePath):

    audioFile = eyed3.load(filePath)

    global db
    
    parameters = ""
    values = ""
    
    genre = None
    title = audioFile.tag.title
    artist = audioFile.tag.artist
    album = audioFile.tag.album
    if audioFile.tag.genre is not None:
        genre = audioFile.tag.genre.name
    
    
    if title is not None:
        parameters += "title, "
        values += "\"" + title + "\", "
    if artist is not None:
        parameters += "artist, "
        values += "\"" + artist + "\", "
    if album is not None:
        parameters += "album, "
        values += "\"" + album+ "\", "
    if genre is not None:
        parameters += "genre, "
        values += "\"" + genre + "\", "
    if filePath is not None:
        parameters += "filePath, "
        values += "\"" + filePath + "\", " 
    
    parameters = parameters[0: len(parameters) - 2]
    values = values[0: len(values) - 2]
    
    sqlStr = "INSERT INTO Songs (%s) VALUES (%s)" % (parameters, values)
    
    try:
        db = lite.connect(dbName)
        cursor = db.cursor()
        cursor.execute(sqlStr)
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
        
def updateSong(dbName, id, title = None, artist = None, album = None, genre = None, filePath = None):
    global db
    
    values = ""
    
    if title != None:
        values += "title ='"+ title +"', "
    if artist != None:
        values += "artist ='"+ artist +"', "
    if album != None:
        values += "album ='"+ album +"', "        
    if genre != None:
        values += "genre ='"+ genre +"', "
    if filePath != None:
        values += "filePath ='"+ genre +"', "    
        
    values = values[0: len(values) - 2]   
    
    sqlStr  = "UPDATE Songs SET %s WHERE id = %d" % (values, id)
    print sqlStr
    
    if values != "":    
        try:
            db = lite.connect(dbName)
            cursor = db.cursor()
            cursor.execute(sqlStr)
            
            db.commit()
        
        except lite.Error, e:
            print "Error: %s" %e.args[0]
            print sys.exc_traceback.tb_lineno
            raise e
        finally:
            db.close()
            
def getLastSongRow(dbName):
    global db

    try:
        db = lite.connect(dbName)
        cursor = db.cursor()
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