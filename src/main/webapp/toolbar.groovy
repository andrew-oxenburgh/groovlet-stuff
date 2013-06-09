import groovy.transform.Field

@Grab(group = 'junit', module = 'junit', version = '4.11') @Field Map myParams
@Field String content = ""
@Field List tabs = new ArrayList()
@Field File file
@Field String toolname

myParams = params as Map

toolname = myParams.get('tool')

if (myParams.containsKey("tool")) {
    file = "src\\main\\groovy\\${toolname}.groovy" as File
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

body,input{
    font-family:"courier";
    font-size:15px;
    line-height:4px;
}

.selected {
    font-weight:bold;
}

h1{
    color:cyan;
    line-height:25px;
    font-weight:bold;
}

   """)
    }
    body {
        h1 't-is-for-toolbar'
        tabs.each {
            String tab ->
                if (toolname == tab) {
                    span('class': 'selected', {
                        a(href: "toolbar.groovy?tool=$tab", tab)
                    })

                } else {
                    a(href: "toolbar.groovy?tool=$tab", tab)
                }
        }
        if (myParams.containsKey("tool")) {
            html.form {
                p(help())
                input('type': 'text', 'name': 'args', 'value':myParams.get("args"))
                input('type': 'hidden', 'name': 'tool', 'value': myParams.get("tool"))
                input('type': 'submit', 'name': 'go', 'value': 'go')
            }
        }
        if (myParams.containsKey("go")) {
            hr {}
            content.split('\n').each {
                p(it)
            }
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

String scriptText(File script, String method, Object... prms) {
    def buf = new ByteArrayOutputStream()
    def newOut = new PrintStream(buf)
    def saveOut = System.out
    System.out = newOut
    new GroovyShell().parse(script).invokeMethod(method, prms)
    System.out = saveOut
    buf.toString()
}
