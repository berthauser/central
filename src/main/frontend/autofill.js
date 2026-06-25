(function() {
    const css = [
        'input:-webkit-autofill,',
        'input:-webkit-autofill:hover,',
        'input:-webkit-autofill:focus,',
        'input:-webkit-autofill:active {',
        '    -webkit-box-shadow: 0 0 0 1000px #000 inset !important;',
        '    -webkit-text-fill-color: #fff !important;',
        '    caret-color: #fff !important;',
        '}'
    ].join('\n');

    function inject(root) {
        var s = document.createElement('style');
        s.textContent = css;
        root.appendChild(s);
    }

    function processField(el) {
        if (el.shadowRoot) {
            var container = el.shadowRoot.querySelector('vaadin-input-container');
            if (container && container.shadowRoot) {
                inject(container.shadowRoot);
            }
        }
    }

    document.querySelectorAll('vaadin-text-field, vaadin-password-field').forEach(processField);

    var observer = new MutationObserver(function(muts) {
        for (var i = 0; i < muts.length; i++) {
            for (var j = 0; j < muts[i].addedNodes.length; j++) {
                var n = muts[i].addedNodes[j];
                if (n.nodeType === Node.ELEMENT_NODE) {
                    if (n.matches && (n.matches('vaadin-text-field') || n.matches('vaadin-password-field'))) {
                        processField(n);
                    }
                    if (n.querySelectorAll) {
                        n.querySelectorAll('vaadin-text-field, vaadin-password-field').forEach(processField);
                    }
                }
            }
        }
    });
    if (document.body) observer.observe(document.body, { childList: true, subtree: true });
    else document.addEventListener('DOMContentLoaded', function() { observer.observe(document.body, { childList: true, subtree: true }); });
})();
