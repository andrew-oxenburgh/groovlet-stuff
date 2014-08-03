package org.adscale.gerald

class GeraldGraph {
    static def graph() {
        $/            <h1>
                'hello, world'
            </h1>

            <div id="placeholder" style="width:1024px;height:600px">
                <script>
$.plot($("#placeholder"),  [{  label: "some numbers", data:[[0, 0], [1, 1], [2, 2], [12, 32]]}]);
                </script>

            </div>
/$
    }
}
