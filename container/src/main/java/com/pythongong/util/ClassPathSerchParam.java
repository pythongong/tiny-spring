package com.pythongong.util;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import lombok.Builder;

@Builder
public record ClassPathSerchParam(String packagePath, BiConsumer<Path, Path> pathMapper, boolean searchSudDirect, boolean serachFile, boolean serachJar) {
    
}
