package ua.edu.ethnographyresearch;

import android.webkit.MimeTypeMap;

import com.github.file_picker.FileType;

import java.io.File;

public class Utils {

    public static FileType getFileType(String path) {
        var file = new File(path);
        var type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExt(sanitizeFileName(file.getName())));
        assert type != null;

        if(type.contains("image")) {
            return FileType.IMAGE;
        }else if(type.contains("video")){
            return FileType.VIDEO;
        }else if(type.contains("audio")){
            return FileType.AUDIO;
        }
        return FileType.IMAGE;
    }

    public static String sanitizeFileName(String name)
    {
        byte[] invalidChars = new byte[]{ (byte)' ', 34, 60, 62, 124, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 58, 42, 63, 92, 47};
        for(byte i : invalidChars)
        {
            name = name.replace((char)i,'_');
        }
        return name;
    }

    public static String getFileExt(String str){
        String[] fileArr = str.split("\\.");
        String extention = fileArr[fileArr.length - 1];
        return extention;
    }
}
