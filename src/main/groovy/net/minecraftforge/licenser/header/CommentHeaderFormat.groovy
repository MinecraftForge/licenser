/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, Minecrell <https://github.com/Minecrell>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.minecraftforge.licenser.header

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import net.minecraftforge.licenser.util.HeaderHelper

import javax.annotation.Nullable
import java.util.regex.Pattern

@EqualsAndHashCode
class CommentHeaderFormat implements HeaderFormat {

    final String name

    final Pattern start
    @Nullable
    final Pattern end
    @Nullable
    final Pattern skipLine

    final String firstLine
    final String prefix
    final String lastLine

    @PackageScope
    CommentHeaderFormat(String name, Pattern start, @Nullable Pattern end, @Nullable Pattern skipLine,
                        String firstLine, String prefix, String lastLine) {
        this.name = name
        this.start = start
        this.end = end
        this.skipLine = skipLine
        this.firstLine = firstLine
        this.prefix = prefix
        this.lastLine = lastLine
    }

    protected List<String> format(String text) {
        ensureAbsent(text, firstLine)
        ensureAbsent(text, lastLine)

        List<String> result = [firstLine]

        text.eachLine {
            result << HeaderHelper.stripTrailingIndent("$prefix $it")
        }

        result << lastLine
        return result
    }

    private static void ensureAbsent(String s, String search) {
        if (s.contains(search)) {
            throw new IllegalArgumentException("Header contains unsupported characters $search")
        }
    }

    @Override
    PreparedHeader prepare(Header header, String text) {
        return new PreparedCommentHeader(header, this, format(text))
    }

    @Override
    String toString() {
        return name
    }

}
