Add-Type -AssemblyName System.Drawing

$srcPath = "C:\Users\Haris Laptops\.gemini\antigravity\brain\39b9a3d7-1c74-420f-80fa-ad1573a0de14\.user_uploaded\media_1795260711908_f3165464.jpg"
$baseDir = "C:\Users\Haris Laptops\Documents\personal repository\numi"
$resDir  = "$baseDir\app\src\main\res"

$src = [System.Drawing.Bitmap]::FromFile($srcPath)

# Bounding box of N with padding:
# x: 260 to 764 (width 504)
# y: 280 to 744 (height 464)
$boxW = 504
$boxH = 464
$startX = 260
$startY = 280

# Create 512x512 transparent canvas
$canvas = New-Object System.Drawing.Bitmap(512, 512, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
$offX = [math]::Round((512 - $boxW) / 2) # 4
$offY = [math]::Round((512 - $boxH) / 2) # 24

for ($y = 0; $y -lt $boxH; $y++) {
    $sy = $startY + $y
    $dy = $offY + $y
    for ($x = 0; $x -lt $boxW; $x++) {
        $sx = $startX + $x
        $dx = $offX + $x

        $c = $src.GetPixel($sx, $sy)

        # Brightness
        $lum = 0.299 * $c.R + 0.587 * $c.G + 0.114 * $c.B

        # Pure background has lum < 70
        if ($lum -lt 72) {
            $canvas.SetPixel($dx, $dy, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
        } elseif ($lum -gt 135) {
            $canvas.SetPixel($dx, $dy, [System.Drawing.Color]::FromArgb(255, $c.R, $c.G, $c.B))
        } else {
            # Smooth edge transition
            $t = ($lum - 72.0) / (135.0 - 72.0)
            $alpha = [int]($t * 255.0)
            # Enhance cyan luminance on edge pixels so dark background doesn't darken the fringe
            $r = [math]::Min(255, [int]($c.R + (1.0 - $t) * 40))
            $g = [math]::Min(255, [int]($c.G + (1.0 - $t) * 60))
            $b = [math]::Min(255, [int]($c.B + (1.0 - $t) * 80))
            $canvas.SetPixel($dx, $dy, [System.Drawing.Color]::FromArgb($alpha, $r, $g, $b))
        }
    }
}

# Save high-res app_icon.png for in-app use (Splash screen, Top Bar, About screen)
$canvas.Save("$resDir\drawable\app_icon.png", [System.Drawing.Imaging.ImageFormat]::Png)
Write-Host "Saved clean app_icon.png"

# Helper for high quality resizing
function Resize-Bmp($orig, $w, $h) {
    $bmp = New-Object System.Drawing.Bitmap($w, $h, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $g.DrawImage($orig, 0, 0, $w, $h)
    $g.Dispose()
    return $bmp
}

# Update all mipmaps with the pure transparent N logo (no box, no black background!)
$densities = @{
    "mipmap-mdpi"    = 48
    "mipmap-hdpi"    = 72
    "mipmap-xhdpi"   = 96
    "mipmap-xxhdpi"  = 144
    "mipmap-xxxhdpi" = 192
}

foreach ($entry in $densities.GetEnumerator()) {
    $folder = $entry.Key
    $sz     = $entry.Value
    $targetDir = "$resDir\$folder"

    $icon = Resize-Bmp $canvas $sz $sz
    $icon.Save("$targetDir\ic_launcher.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $icon.Save("$targetDir\ic_launcher_round.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $icon.Dispose()

    Write-Host "Saved $folder ($sz x $sz)"
}

$canvas.Dispose()
$src.Dispose()
Write-Host "DONE: All icons generated without box or black background!"
