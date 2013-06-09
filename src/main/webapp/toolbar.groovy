import groovy.transform.Field

@Grab(group='junit', module='junit', version='4.11')


import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

@Field Map myParams


myParams = params as Map

@Field String content = ""
@Field List tabs = new ArrayList()

@Field File file
if (myParams.containsKey("tool")) {
    if (myParams.containsKey("go")) {
        file = "src\\main\\groovy\\${myParams.get("tool")}.groovy" as File
        content = showPage(file)
    }
}

String showPage(File file) {
    def buf = new ByteArrayOutputStream()
    def newOut = new PrintStream(buf)
    def saveOut = System.out
    System.out = newOut
    new GroovyShell().run(file, myParams.get('args').split(' '))
    System.out = saveOut
    buf.toString()
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
        if (myParams.containsKey("tool")) {
            if (myParams.containsKey("go")) {
                p(content)
            } else {
                html.form {
//                    p(helpText())
//                    p(helpText())
                    input('type': 'text', 'name': 'args')
                    input('type': 'hidden', 'name': 'tool', 'value': myParams.get("tool"))
                    input('type': 'submit', 'name': 'go', 'value': 'go')
                }
            }
        }
    }
}

private Object helpText() {
    try {
        assertNotNull file
        assertTrue file.exists()
        Script script = new GroovyShell().parse(file)
        script.invokeMethod('help', [])
    } catch (RuntimeException e) {
        e.printStackTrace()
        return 'help not found'
    }
}