config.plugins.push({
    apply: (compiler) => {
        compiler.hooks.done.tap('DebugPlugin', (stats) => {
            if (stats.hasErrors()) {
                console.error("=== WEBPACK ERRORS ===");
                console.error(stats.toString({ all: false, errors: true, errorDetails: true }));
            }
        });
    }
});
