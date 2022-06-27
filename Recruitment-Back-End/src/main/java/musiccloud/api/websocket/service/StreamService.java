package musiccloud.api.websocket.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import musiccloud.api.websocket.config.FirebaseConfig;
import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.AudioFile;
import musiccloud.api.websocket.repository.jpa.AppUserRepository;
import musiccloud.api.websocket.repository.jpa.AudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
public class StreamService {
    private FirebaseConfig config;
    private AppUserRepository appUserRepository;
    private AudioRepository audioRepository;

    @Autowired
    public StreamService(FirebaseConfig config, AppUserRepository appUserRepository, AudioRepository audioRepository){
        this.config = config;
        this.appUserRepository = appUserRepository;
        this.audioRepository = audioRepository;
    }

    public AudioFile audioFile(String id){
        return audioRepository.getById(id);
    }

    public byte[] download(String songUuid) {
        Storage storage = StorageClient.getInstance().bucket().getStorage();
        BlobId blobId = BlobId.of(config.bucketName(), songUuid);
        Blob blob = storage.get(blobId);
        return blob.getContent();
    }

    public void upload(MultipartFile multipartFile, String filename, String userID) throws IOException {
        File file = null;
        Storage storage = null;
        storage = StorageClient.getInstance().bucket().getStorage();
        String randomID = UUID.randomUUID().toString();
        BlobId blobId = BlobId.of(config.bucketName(),randomID);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("audio/mpeg").build();
        try {
            file = convertToFile(multipartFile,filename);
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        AudioFile audioFile = null;

        try {
            audioFile = entityFromFile(file);
            audioFile.setSharable(true);
            audioFile.setId(blobInfo.getBlobId().getName());
            audioFile.setName(filename);
            audioFile.setTimeUploaded(Timestamp.from(Instant.now()));
        } catch (InvalidDataException | UnsupportedTagException e) {
            storage.delete(blobInfo.getBlobId());
            e.printStackTrace();
            return;
        }

        AppUser user = appUserRepository.getById(userID);
        user.getUserUploads().add(audioFile);
        user = appUserRepository.saveAndFlush(user);
        file.delete();

    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private AudioFile entityFromFile(File file) throws InvalidDataException, IOException, UnsupportedTagException {
        {

            AudioFile audioFile = new AudioFile();
            Mp3File mp3File = new Mp3File(file);

            if (mp3File.hasId3v2Tag()) {
                ID3v2 id3v2 = mp3File.getId3v2Tag();
                byte[] albumImage = id3v2.getAlbumImage();
                String album = id3v2.getAlbum();
                String title = id3v2.getTitle();
                String genre = id3v2.getGenreDescription();
                String artist = id3v2.getArtist();
                audioFile.setAlbumImage(albumImage);
                audioFile.setAlbum(album);
                audioFile.setTitle(title);
                audioFile.setGenre(genre);
                audioFile.setArtist(artist);
            } else {
                throw new UnsupportedTagException();
            }

            return audioFile;

        }
    }

}
