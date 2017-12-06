package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.NowPlayingSong;

/**
 * Created by rynfd on 12/3/2017.
 */

public class GetNowPlayingResponse extends GetResponseTemplate {

        @JsonCreator
        public GetNowPlayingResponse(@JsonProperty("error") boolean error,
                                     @JsonProperty("error_message") String error_message,
                                     @JsonProperty("data") JsonNode data) {
            this.error = error;
            this.errorMessage = error_message;
            this.data = data;
        }

        public NowPlayingSong getNowPlaying() {

            String currentRequestID = data.get("request_id").asText();

            if(currentRequestID.equals("false")){
                NowPlayingSong emptySong = new NowPlayingSong("-1", "",
                        "", "", "", "", "null");
                return emptySong;
            }

            String currentMusicID = data.get("musics_id").asText();
            String currentAlbum = data.get("album").asText();
            String currentArtist = data.get("artist").asText();
            String currentTitle = data.get("title").asText();
            String currentGenre = data.get("genre").asText();
            String currentImg = data.get("base64img").asText();

            NowPlayingSong currentSong = new NowPlayingSong(currentRequestID, currentMusicID,
                    currentTitle, currentArtist, currentAlbum, currentGenre, currentImg);

            return currentSong;
        }
}
