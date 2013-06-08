import groovy.transform.Field

Map myParams = params as Map

@Field String content = ""
@Field List tabs = new ArrayList()

if (myParams.containsKey("tool")) {
    File file = "src\\main\\groovy\\${myParams.get("tool")}.groovy" as File
    def buf = new ByteArrayOutputStream()
    def newOut = new PrintStream(buf)
    def saveOut = System.out
    System.out = newOut
    new GroovyShell().run(file)
    System.out = saveOut
    content = buf.toString()
}

new File("src/main/groovy").listFiles().each {
    File tool ->
        if (!tool.name.endsWith('.groovy')) {
            return
        }
        tabs.add(tool.name - '.groovy')
}



html.html {    // html is implicitly bound to new MarkupBuilder(out)
    head {
        title("t-is-for-toolbar")
    }
    body {
        p(tabs.each {
            a(href: "toolbar.groovy?tool=$it", it)
        })
        p(content)
    }
}

