package app.vanadium.config.host;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import app.vanadium.config.proto.VanadiumConfigProto.Configs;

public class ConfigParser {
    public static void main(String[] args) throws IOException {
        String inFile = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-i".equals(arg) || "--in_file".equals(arg)) {
                inFile = args[i + 1];
                break;
            } else if (arg.startsWith("--in_file=")) {
                inFile = arg.split("=")[1];
                break;
            }
        }
        if (inFile == null || inFile.isBlank() || inFile.isEmpty()) {
            throw new IllegalArgumentException("No input file supplied, exiting");
        }
        try (var inputStream = new FileInputStream(inFile)) {
            var configs = Configs.parseFrom(inputStream);
            System.out.println(configs.toString());
        }
    }
}
