import eyed3

audiofile = eyed3.load("Foster the People - Call it What You Want.mp3")
print audiofile.tag.title
print audiofile.tag.album
print audiofile.tag.track_num
print audiofile.tag.artist
print audiofile.tag.play_count
print audiofile.tag.genre.name
print audiofile.tag.genre.id

#title, album, track_num, artist, play_count, genre, 