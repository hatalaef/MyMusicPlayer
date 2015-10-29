import eyed3
import sqlite3 as lite
import sys

db = None

def addSongToDb(dbName, audioFile):

    global db
    
    try:
        db = lite.connect(dbName)
        cursor = db.cursor()
        cursor.execute("INSERT OR IGNORE INTO Songs(title, artist, album, genre) VALUES (?, ?, ?, ?)",
        (audioFile.tag.title, audioFile.tag.artist, audioFile.tag.album, audioFile.tag.genre.name))
        
        db.commit()
        
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
        
def updateSong(dbName, id, title = None, artist = None, album = None, genre = None):
    global db
    
    values = ""
    
    if title != None:
        values += "title ='"+title+"', "
    if artist != None:
        values += "artist ='"+artist+"', "
    if album != None:
        values += "album ='"+album+"', "        
    if genre != None:
        values += "genre ='"+genre+"', "
        
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