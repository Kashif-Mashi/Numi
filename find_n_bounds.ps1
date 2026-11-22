Add-Type -AssemblyName System.Drawing

$srcPath = "C:\Users\Haris Laptops\.gemini\antigravity\brain\39b9a3d7-1c74-420f-80fa-ad1573a0de14\.user_uploaded\media_1795260711908_f3165464.jpg"
$src = [System.Drawing.Bitmap]::FromFile($srcPath)

# Check bounds where brightness is high (N glyph)
# Inside the squircle: from x=200 to 824, y=200 to 824
$minX = 1024; $maxX = 0; $minY = 1024; $maxY = 0

for ($y = 200; $y -lt 824; $y++) {
    for ($x = 200; $x -lt 824; $x++) {
        $c = $src.GetPixel($x, $y)
        # The N is bright white/cyan: R > 120 and G > 170 and B > 180
        if ($c.G -gt 160 -and $c.B -gt 180) {
            if ($x -lt $minX) { $minX = $x }
            if ($x -gt $maxX) { $maxX = $x }
            if ($y -lt $minY) { $minY = $y }
            if ($y -gt $maxY) { $maxY = $y }
        }
    }
}

Write-Host "N bounds: $minX, $minY to $maxX, $maxY (Width: $($maxX-$minX), Height: $($maxY-$minY))"
$src.Dispose()
