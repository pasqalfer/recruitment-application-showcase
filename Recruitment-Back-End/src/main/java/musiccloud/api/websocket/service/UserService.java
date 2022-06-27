package musiccloud.api.websocket.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.AudioFile;
import musiccloud.api.websocket.entity.Playlist;
import musiccloud.api.websocket.entity.RecentlyPlayedFeed;
import musiccloud.api.websocket.model.*;
import musiccloud.api.websocket.repository.jpa.AppUserRepository;
import musiccloud.api.websocket.repository.jpa.AudioRepository;
import musiccloud.api.websocket.repository.jpa.PlaylistRepository;
import musiccloud.api.websocket.repository.neo4j.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Responsible for :
 * > registering new user
 * > validating user credentials and issue tokens
 * > Store and retrieve user profiles
 * > Send message whenever a user is created/updated
 */
@Service
public class UserService {
    private final AppUserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final PlaylistRepository playlistRepository;

    private final AudioRepository audioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Lazy
    private final UserDetailsServiceImpl userDetailsService;

    public void register(AppUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString());
        userRepository.save(user);
    }

    @Autowired
    public UserService( AppUserRepository userRepository, ProfileRepository profileRepository, PlaylistRepository playlistRepository, AudioRepository audioRepository, UserDetailsServiceImpl userDetailsService){
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.playlistRepository = playlistRepository;
        this.audioRepository = audioRepository;
        this.userDetailsService = userDetailsService;
    }

    public void uploadAudio(String userID, byte [] file){
        AudioFile audioFile = new AudioFile();
        AppUser appUser = findUserById(userID);
        if(appUser.getUserUploads() == null)
            appUser.setUserUploads(new ArrayList<>());
        appUser.getUserUploads().add(audioFile);
    }

    public Boolean registerUser(String email, String username, String password){
        String randomUID = UUID.randomUUID().toString();
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisplayName(username)
                .setUid(randomUID)
                .setDisabled(false);
        try {
            UserRecord record = FirebaseAuth.getInstance().createUser(request);
            AppUser dto = AppUser.builder()
                    .id(record.getUid())
                    .username(username)
                    .email(email)
                    .userUploads(new ArrayList<>())
                    .playlists(new ArrayList<>())
                    .build();
            userRepository.save(dto);

            Profile profile = Profile.builder().id(randomUID).followers(new HashSet<>()).build();
            addProfile(profile);

           // if(!profileAddedSuccessfully){

          //  }

            // send message here

            return true;
        } catch (FirebaseAuthException e) {
            try {
                FirebaseAuth.getInstance().deleteUser(randomUID);
            } catch (FirebaseAuthException firebaseAuthException) {
                firebaseAuthException.printStackTrace();
            }
            userRepository.deleteById(randomUID);
            return false;
        }
    }

    public AppUser findUserById(String id){
        return userRepository.findById(id).get();
    }

    public AppUser findByUsername(String name){
        return userRepository.findAppUserByUsername(name);
    }

    public AppUser updateUser(AppUser appUser){
        return userRepository.save(appUser);
    }

    public AppUser addUserPlaylist(String username, String playlistName, boolean sharable){
        AppUser appUserEntity = userRepository.findAppUserByUsername(username);
        if(Objects.isNull(appUserEntity)){
            // throw error
            return null;
        }

        Playlist playlistEntity = new Playlist();
        playlistEntity.setOwner(appUserEntity);
        playlistEntity.setName(playlistName);
        playlistEntity.setSharable(sharable);
        appUserEntity.getPlaylists().add(playlistEntity);
        return userRepository.save(appUserEntity);
    }

    public void addSongToPlaylist(String username, String playlistId, String audioID){
        AppUser appUserEntity = userRepository.findAppUserByUsername(username);
        AudioFile audioFileEntity = audioRepository.findById(audioID).get();

        List<Playlist> userPlaylist = appUserEntity.getPlaylists();
        boolean hasAccess = false;
        for(Playlist e : userPlaylist){
            if(e.getId().equals(playlistId)){
                e.getPlaylistSongs().add(audioFileEntity);
                hasAccess = true;
                break;
            }
        }

        if(hasAccess){
            userRepository.save(appUserEntity);
        }
    }

    @Transactional
    public String addRecentlyPlayed(String username, String audioID){
        AudioFile audioFile = audioRepository.findById(audioID).get();
        AppUser appUser = findByUsername(username);
        RecentlyPlayedFeed recentlyPlayed = new RecentlyPlayedFeed();
        recentlyPlayed.setPlayedFile(audioFile);
        appUser.getRecentlyPlayedFeeds().add(recentlyPlayed);
        userRepository.save(updateUser(appUser));
        return appUser.getId();
    }

    @Transactional
    public boolean addProfileFollower(String followedId, String followingId){
        boolean followerExists = userRepository.existsById(followedId);
        boolean followingExists = userRepository.existsById(followingId);
        if(followerExists && followingExists){
            Profile followed = findProfileById(followedId);
            Profile following = findProfileById(followingId);
            if(Objects.nonNull(followed) && Objects.nonNull(following)){
                Follows f = Follows.builder().followingProfile(following).timeFollowed(LocalDateTime.now().toString()).build();
                followed.getFollowers().add(f);
                profileRepository.save(followed);
                return true;
            }
        }
        return false;
    }

    public boolean addProfile(Profile user){
        if(profileRepository.findById(user.getId()).isPresent()) {
            return false;
        }

        Profile profile = profileRepository.save(user);
        return true;
    }

    public Playlist findUserPlaylistById(String playlistId){
        Playlist playlist = playlistRepository.findById(playlistId).get();
        AppUser appUser = playlistRepository.findOwnerById(playlistId);
        playlist.setOwnerName(appUser.getUsername());
        return playlist;
    }

    public Profile findProfileById(String userId){
        return profileRepository.findById(userId)
                .orElse(null);
    }

    public List<AppUser> searchByName(String name){
        return userRepository.findByEmailContainingOrUsernameContaining(name,name);
    }

    public AudioFile findMostRecentlyPlayedByUsername(String username){
        AppUser appUser = findByUsername(username);
        RecentlyPlayedFeed mostRecentTrack = Collections.max(appUser.getRecentlyPlayedFeeds(), Comparator.comparing(c -> c.getTimePlayed()));
        return mostRecentTrack.getPlayedFile();
    }
}
