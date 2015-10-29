import eyed3
import sqlite3 as lite
import musicDatabase
import sys

db = "music.db"

audioFile = eyed3.load("Foster the People - Call it What You Want.mp3")

musicDatabase.addSongToDb(db, audioFile)

song = musicDatabase.findSongByTitle(db, audioFile.tag.title)

for item in song:
    print item

print    
    
musicDatabase.updateSong(db, song[0], album="new", genre="Alternative")

song = musicDatabase.findSongByTitle(db, audioFile.tag.title)

for item in song:
    print item


