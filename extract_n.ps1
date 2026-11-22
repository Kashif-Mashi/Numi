Add-Type -AssemblyName System.Drawing

$srcPath = "C:\Users\Haris Laptops\.gemini\antigravity\brain\39b9a3d7-1c74-420f-80fa-ad1573a0de14\.user_uploaded\media_1795260711908_f3165464.jpg"
$src = [System.Drawing.Bitmap]::FromFile($srcPath)

# Bounding box of N:
# x: 280 to 744, y: 300 to 724
# Width = 464, Height = 424

# Let's create an extracted bitmap of 512x512
$extracted = New-Object System.Drawing.Bitmap(512, 512, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)

# Offset to center the 464x424 N in the 512x512 bitmap:
$offsetX = [math]::Round((512 - 464) / 2) # 24
$offsetY = [math]::Round((512 - 424) / 2) # 44

for ($y = 0; $y -lt 424; $y++) {
    $srcY = 300 + $y
    $dstY = $offsetY + $y
    for ($x = 0; $x -lt 464; $x++) {
        $srcX = 280 + $x
        $dstX = $offsetX + $x

        $c = $src.GetPixel($srcX, $srcY)

        # The N logo is composed of white & cyan pixels.
        # Background is dark blue / teal gradient:
        # Near top: R ~ 35, G ~ 30, B ~ 116 (brightness ~ 50)
        # Near bottom: R ~ 15, G ~ 70, B ~ 118 (brightness ~ 65)
        # Inside the N strokes: R > 200, G > 220, B > 220 (brightness > 210)
        # Outer glow of the N: cyan (G ~ 180-240, B ~ 220-255, R ~ 80-180)
        
        # Calculate brightness / intensity of the glyph
        # We can calculate how much the pixel belongs to the N vs the dark background:
        # Distance from dark bg in RGB space:
        # Dark bg has low R and low G. N has high R and high G!
        $rg = ($c.R + $c.G) / 2.0
        
        if ($rg -gt 130) {
            # Fully inside the white/bright cyan stroke
            $extracted.SetPixel($dstX, $dstY, [System.Drawing.Color]::FromArgb(255, $c.R, $c.G, $c.B))
        } elseif ($rg -gt 85) {
            # Smooth anti-aliased edge / glow
            $alpha = [math]::Min(255, [math]::Max(0, [int](($rg - 85) / (130 - 85) * 255)))
            # Keep the cyan/white color with calculated alpha
            $extracted.SetPixel($dstX, $dstY, [System.Drawing.Color]::FromArgb($alpha, $c.R, $c.G, $c.B))
        } else {
            # Transparent background!
            $extracted.SetPixel($dstX, $dstY, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
        }
    }
}

$extracted.Save("C:\Users\Haris Laptops\Documents\personal repository\numi\extracted_n_test.png", [System.Drawing.Imaging.ImageFormat]::Png)
Write-Host "Extracted N saved to extracted_n_test.png"

$extracted.Dispose()
$src.Dispose()
