package musiccloud.api.websocket.controller;

import com.google.api.client.util.IOUtils;
import com.google.common.base.Suppliers;
import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.AudioFile;
import musiccloud.api.websocket.entity.Playlist;
import musiccloud.api.websocket.entity.RecentlyPlayedFeed;
import musiccloud.api.websocket.model.*;
import musiccloud.api.websocket.model.payload.AddSongRequest;
import musiccloud.api.websocket.model.payload.CreatePlaylistRequest;
import musiccloud.api.websocket.service.EventService;
import musiccloud.api.websocket.service.StreamService;
import musiccloud.api.websocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final EventService eventService;

    private final StreamService streamService;

    private static final ConcurrentHashMap map = new ConcurrentHashMap();

    @Autowired
    public UserController(EventService eventService, UserService userService, StreamService streamService){
        this.eventService = eventService;
        this.userService = userService;
        this.streamService = streamService;
    }

    @PostMapping(value="/playlists/add" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addPlaylist(Principal principal, @RequestBody CreatePlaylistRequest playlist) {
        AppUser success = userService.addUserPlaylist(principal.getName(), playlist.name, playlist.shareable);
        if(success != null) {
            eventService.triggerEvent(UserUpdateEvent.builder().source(this).object(success).build());
        }
    }

    @PostMapping("/audio/upload")
    public ResponseEntity uploadAudio(Principal principal, @RequestBody MultipartFile audioFile) {
        AppUser appUser = userService.findByUsername(principal.getName());
        try {
            streamService.upload(audioFile,audioFile.getName(),appUser.getId());
            eventService.triggerEvent(UserUpdateEvent.builder().source(this).object(appUser).build());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/audio/{id}/play")
    public byte [] playSong(Principal principal, @PathVariable("id") String id) {
        String username = principal.getName();
        String userID = userService.addRecentlyPlayed(username,id);
        AppUser user = userService.findUserById(userID);
        AudioFile audioFile = streamService.audioFile(id);
        byte [] data = streamService.download(id);
        AudioDTO dto = AudioDTO
                .builder()
                .metadata(audioFile)
                .url("http://localhost:8080/api/user/audio/" + id+  "/download")
                .username(username)
                .build();
        eventService.triggerEvent(UserUpdateEvent.builder().source(this).object(user).build());
        eventService.triggerEvent(PlayTrackEvent.builder().source(this).object(dto).build());

    //    return toFluxByteBuffer(inputStream,4096);
        return streamService.download(id);
    }

    @GetMapping("/audio/{id}/download")
    public byte [] downloadSong(Principal principal, @PathVariable("id") String id) {
        return streamService.download(id);
    }
/*
    public static Flux<ByteBuffer> toFluxByteBuffer(InputStream inputStream, int chunkSize) {
        if (chunkSize <= 0) {
            return Flux.error(new IllegalArgumentException("'chunkSize' must be greater than 0."));
        }

        if (inputStream == null) {
            return Flux.empty();
        }

        return Flux.<ByteBuffer, InputStream>generate(() -> inputStream, (stream, sink) -> {
            byte[] buffer = new byte[chunkSize];

            try {
                int offset = 0;

                while (offset < chunkSize) {
                    int readCount = inputStream.read(buffer, offset, chunkSize - offset);

                    // We have finished reading the stream, trigger onComplete.
                    if (readCount == -1) {
                        // If there were bytes read before reaching the end emit the buffer before completing.
                        if (offset > 0) {
                            sink.next(ByteBuffer.wrap(buffer, 0, offset));
                        }
                        sink.complete();
                        return stream;
                    }

                    offset += readCount;
                }

                sink.next(ByteBuffer.wrap(buffer));
            } catch (IOException ex) {
                sink.error(ex);
            }

            return stream;
        }).filter(ByteBuffer::hasRemaining);
    }

 */

    @PostMapping("/playlists/add-song")
    public String addSong(Principal principal, @RequestBody AddSongRequest addSongRequest){
        userService.addSongToPlaylist(principal.getName(), addSongRequest.playlistId, addSongRequest.audioId);
        AppUser appUser = userService.findByUsername(principal.getName());
        Playlist playlist = userService.findUserPlaylistById(addSongRequest.playlistId);
        eventService.triggerEvent(UserUpdateEvent.builder().source(this).object(appUser).build());
        eventService.triggerEvent(PlaylistUpdatedEvent.builder().source(this).object(playlist).build());
        return String.valueOf(addSongRequest.playlistId);
    }

    @GetMapping("/playlists/fetch")
    public Playlist addSong(Principal principal, @RequestParam String playlistId){
        return userService.findUserPlaylistById(playlistId);
    }

}
