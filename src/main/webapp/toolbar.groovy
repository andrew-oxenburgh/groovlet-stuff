import groovy.transform.Field

@Grab(group = 'junit', module = 'junit', version = '4.11')

@Field Map myParams
@Field String content = ""
@Field List tabs = new ArrayList()
@Field File file

myParams = params as Map

String scriptText(File script, String method, Object... prms) {
    def buf = new ByteArrayOutputStream()
    def newOut = new PrintStream(buf)
    def saveOut = System.out
    System.out = newOut
    new GroovyShell().parse(script).invokeMethod(method, prms)
    System.out = saveOut
    buf.toString()
}

if (myParams.containsKey("tool")) {
    file = "src\\main\\groovy\\${myParams.get("tool")}.groovy" as File
    if (myParams.containsKey("go")) {
        content = scriptText(file, 'main', myParams.get('args').split(' '))
    }
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
        style("""
body
{
font-family:"courier";
font-size:30px;
}
   """)
    }
    body {
        p(tabs.each {
            a(href: "toolbar.groovy?tool=$it", it)
        })
        if (myParams.containsKey("tool")) {
            html.form {
//                    p(helpText())
                p(help())
                input('type': 'text', 'name': 'args')
                input('type': 'hidden', 'name': 'tool', 'value': myParams.get("tool"))
                input('type': 'submit', 'name': 'go', 'value': 'go')
            }
        }
        if (myParams.containsKey("go")) {
            hr{}
            p(content)
        }
    }
}

private String help() {
    try {
        scriptText(file, 'help', null)
    } catch (RuntimeException e) {
        'no help found'
    }
}
