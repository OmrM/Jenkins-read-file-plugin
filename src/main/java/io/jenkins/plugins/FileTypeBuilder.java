package io.jenkins.plugins;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTypeBuilder extends Builder {
    private FilePath filePath;

    @DataBoundConstructor
    public FileTypeBuilder(FilePath filePath) {
        this.filePath = filePath;
    }

    public FilePath getFilePath() {
        return filePath;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        FileTypeDetectionAction fileTypeDetector = new FileTypeDetectionAction();
        try {
            fileTypeDetector.detectFileTypes(filePath, listener);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        listener.getLogger().println("Amount of Files: " + fileTypeDetector.getTotalCount());

        HashMap<String, Integer> fileCount = fileTypeDetector.getFileCount();
        for (Map.Entry<String, Integer> entry : fileCount.entrySet()) {
            listener.getLogger().println("File type: " + entry.getKey() + ", count: " + entry.getValue());
        }

        return true;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> buildClass) {
            return true;
        }

        //This is what shows up when Adding the Build step
        @Override
        public String getDisplayName() {
            return "File Type Detection";
        }


    }
}
