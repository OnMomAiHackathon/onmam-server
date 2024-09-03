package dto.ai;

import java.io.File;

public class AIDiaryRequest {
    private File audioFile;

    // Getter 및 Setter
    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }
}