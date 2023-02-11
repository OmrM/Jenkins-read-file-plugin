package io.jenkins.plugins;

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.Run;
import hudson.model.TaskListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileTypeDetectionAction implements Action {

    private HashMap<String, Integer> fileCount = new HashMap<>();
    private ArrayList<String> pptxNames = new ArrayList<>();
    private ArrayList<String> docxNames = new ArrayList<>();
    private ArrayList<String> pdfNames = new ArrayList<>();
    private ArrayList<String> unknownNames = new ArrayList<>();
    private int totalCount = 0;

    public HashMap<String, Integer> getFileCount() {
        return fileCount;
    }

    public ArrayList<String> getPptxNames() {
        return pptxNames;
    }

    public ArrayList<String> getDocxNames() {
        return docxNames;
    }

    public ArrayList<String> getPdfNames() {
        return pdfNames;
    }

    public ArrayList<String> getUnknownNames() {
        return unknownNames;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int updateTotalCount() {
        return ++totalCount;
    }

    public void detectFileTypes(FilePath filePath, TaskListener taskListener) throws Exception {
        for (FilePath f : filePath.list()) {
            updateTotalCount();
            String fileName = f.toString();

            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                String extension = fileName.substring(index + 1);
                if (!fileCount.containsKey(extension)) {
                    fileCount.put(extension, 1);
                } else {
                    fileCount.put(extension, fileCount.get(extension) + 1);
                }
            }
        }
        Iterator hmIterator = fileCount.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) hmIterator.next();
            int marks = ((int) mapElement.getValue());
            taskListener.getLogger().println("Amount " + mapElement.getKey() + " : " + marks);
        }
    }

    @Override
    public String getDisplayName() {
        return "File Type Detection Results";
    }

    @Override
    public String getIconFileName() {
        return "/plugin/file-type-detection/icons/file-types.png";
    }

    @Override
    public String getUrlName() {
        return "file-type-detection";
    }
}
