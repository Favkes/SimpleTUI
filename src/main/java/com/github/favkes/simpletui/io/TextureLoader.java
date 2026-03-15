package com.github.favkes.simpletui.io;

import com.github.favkes.simpletui.components.AdvancedTexture;
import com.github.favkes.simpletui.ui.Color;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

public class TextureLoader {
    private static volatile TextureLoader INSTANCE;
    private Path root;

    public static TextureLoader getInstance() {
        if (INSTANCE == null) {
            synchronized (TextureLoader.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TextureLoader();
                }
            }
        }
        return INSTANCE;
    }
    private TextureLoader() {
        root = Paths.get("");
    }

    public void setRoot(Path path) {
        root = path;
    }

    public AdvancedTexture load(String filePath, Class<?> caller) throws IOException {
        InputStream filePathIS = caller.getResourceAsStream(filePath);
//        if (!filePathStr.endsWith(".json")) filePathStr += ".json";
        if (filePathIS == null) throw new FileNotFoundException("Resource not found in the classpath!");

        String contentStr = new String(filePathIS.readAllBytes(), StandardCharsets.UTF_8);

//        Path filePath = root.resolve(filePathStr);
        System.out.printf("loading texture from: %s\n", filePathIS);
//        String contentStr = Files.readString(filePath);
        JSONObject root = new JSONObject(contentStr);
        JSONArray content = root.getJSONArray("content");

        IntUnaryOperator offsetFunction;
        if (root.get("offset") != null) {
            offsetFunction = ExpressionParser.parse((String)root.get("offset")).compile();
        } else offsetFunction = row -> row;

        StringBuilder textureBody = new StringBuilder();

        for (int i=0; i<content.length(); i++) {
            Object obj = content.get(i);
            if (obj instanceof String) {
                textureBody.append((String)obj);
            } else if (obj instanceof JSONObject jsonObj) {
                if (jsonObj.has("bg")) {
                    JSONArray arr = jsonObj.getJSONArray("bg");
                    int[] bg = new int[arr.length()];
                    for (int j=0; j<arr.length(); j++) bg[j] = arr.getInt(j);
                    textureBody.append(Color.generateRGB(true, bg[0], bg[1], bg[2]));
                }
                if (jsonObj.has("fg")) {
                    JSONArray arr = jsonObj.getJSONArray("fg");
                    int[] fg = new int[arr.length()];
                    for (int j=0; j<arr.length(); j++) fg[j] = arr.getInt(j);
                    textureBody.append(Color.generateRGB(false, fg[0], fg[1], fg[2]));
                }
            }
        }

        return new AdvancedTexture(textureBody.toString(), 1, offsetFunction::applyAsInt);
    }
}
