import org.pegdown.*
import org.pegdown.ast.*
import groovy.io.LineColumnReader

def mdproc = new PegDownProcessor(Extensions.ALL & ~Extensions.HARDWRAPS)

def text = '''\
Here's how you can print some text in Groovy:

    println 'hi'

That's quite cool

```groovy
println 'hello'
println 'bye'
```

eq Header 1
=================

hyphen Header 2
--------

# hash h1 #

## h2 ##

### h3 ###

*italic*

_bold_

*   Abacus
    * answer
*   Bubbles
    1.  bunk
    2.  bupkis
        * BELITTLER
    3. burper
*   Cunning



done'''

def lineOffsets = [:]
def lineColReader = new LineColumnReader(new StringReader(text)).with { r ->
    int offset = 0
    r.eachLine { line ->
        offset += r.column
        lineOffsets[(offset)] = r.line
    }
}

def root = mdproc.parseMarkdown(text.toCharArray())

def serializer = new ToHtmlSerializer(null) {
    StringBuilder script = new StringBuilder()
    private int line = 0

    void visit(VerbatimNode node) {
        def lineOffset = lineOffsets[node.startIndex]
        if (line < lineOffset) {
            script << '\n' * (lineOffset - line)
            line = lineOffset
        }
        node.text.eachLine { script << "    $it\n" }
        super.visit(node)
    }
}

def html = serializer.toHtml(root)
def script = serializer.script.toString()

println """HTML output:
${'=' * 80}
${html}
${'=' * 80}
"""
