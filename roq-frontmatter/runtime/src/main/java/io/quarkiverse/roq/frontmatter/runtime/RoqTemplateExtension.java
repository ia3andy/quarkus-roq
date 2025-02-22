package io.quarkiverse.roq.frontmatter.runtime;

import java.util.List;
import java.util.regex.Pattern;

import io.quarkiverse.roq.frontmatter.runtime.model.*;
import io.quarkiverse.roq.util.PathUtils;
import io.quarkus.qute.TemplateExtension;
import io.vertx.core.json.JsonArray;

@TemplateExtension
public class RoqTemplateExtension {

    private static final int QUTE_FALLBACK_PRIORITY = -2;

    private static final Pattern COUNT_WORDS = Pattern.compile("\\b\\w+\\b");

    public static long numberOfWords(String text) {
        return COUNT_WORDS.matcher(text).results().count();
    }

    @TemplateExtension(matchName = "*", priority = QUTE_FALLBACK_PRIORITY)
    public static RoqCollection collection(RoqCollections collections, String key) {
        return collections.get(key);
    }

    public static Object readTime(Page page) {
        final long count = numberOfWords(page.rawContent());
        return Math.round((float) count / 200);
    }

    public static String slugify(String text) {
        return PathUtils.slugify(text, false, false);
    }

    @SuppressWarnings("unchecked")
    public static List<String> asStrings(Object o) {
        if (o instanceof String i) {
            return List.of((i).split("\\h*,\\h*|\\h{2,}"));
        }
        if (o instanceof JsonArray i) {
            return i.getList();
        }
        return List.of();
    }
}
