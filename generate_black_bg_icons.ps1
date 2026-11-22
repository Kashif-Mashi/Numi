Add-Type -AssemblyName System.Drawing

$srcPath = "C:\Users\Haris Laptops\.gemini\antigravity\brain\39b9a3d7-1c74-420f-80fa-ad1573a0de14\.user_uploaded\media_1795260711908_f3165464.jpg"
$baseDir = "C:\Users\Haris Laptops\Documents\personal repository\numi"
$resDir  = "$baseDir\app\src\main\res"

$src = [System.Drawing.Bitmap]::FromFile($srcPath)

# Bounding box of N in original 1024x1024 image:
# x: 260 to 764 (width 504)
# y: 280 to 744 (height 464)
$boxW = 504
$boxH = 464
$startX = 260
$startY = 280

# 1. Create transparent pure N glyph on 512x512
$pureN = New-Object System.Drawing.Bitmap(512, 512, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
$offX = [math]::Round((512 - $boxW) / 2)
$offY = [math]::Round((512 - $boxH) / 2)

for ($y = 0; $y -lt $boxH; $y++) {
    $sy = $startY + $y
    $dy = $offY + $y
    for ($x = 0; $x -lt $boxW; $x++) {
        $sx = $startX + $x
        $dx = $offX + $x

        $c = $src.GetPixel($sx, $sy)
        $lum = 0.299 * $c.R + 0.587 * $c.G + 0.114 * $c.B

        if ($lum -lt 72) {
            $pureN.SetPixel($dx, $dy, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
        } elseif ($lum -gt 135) {
            $pureN.SetPixel($dx, $dy, [System.Drawing.Color]::FromArgb(255, $c.R, $c.G, $c.B))
        } else {
            $t = ($lum - 72.0) / (135.0 - 72.0)
            $alpha = [int]($t * 255.0)
            $r = [math]::Min(255, [int]($c.R + (1.0 - $t) * 40))
            $g = [math]::Min(255, [int]($c.G + (1.0 - $t) * 60))
            $b = [math]::Min(255, [int]($c.B + (1.0 - $t) * 80))
            $pureN.SetPixel($dx, $dy, [System.Drawing.Color]::FromArgb($alpha, $r, $g, $b))
        }
    }
}

# Save pure N for in-app drawables (splash screen, top bar, about)
$pureN.Save("$resDir\drawable\app_icon.png", [System.Drawing.Imaging.ImageFormat]::Png)
$pureN.Save("$resDir\drawable\pure_n_foreground.png", [System.Drawing.Imaging.ImageFormat]::Png)
Write-Host "Saved app_icon.png and pure_n_foreground.png"

# Helper for high quality resizing with optional background
function Create-Icon($orig, $size, $bgType) {
    $bmp = New-Object System.Drawing.Bitmap($size, $size, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality

    if ($bgType -eq "black_square") {
        $brush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(255, 0, 0, 0))
        $g.FillRectangle($brush, 0, 0, $size, $size)
        $brush.Dispose()
    } elseif ($bgType -eq "black_round") {
        $brush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(255, 0, 0, 0))
        $g.FillEllipse($brush, 0, 0, $size, $size)
        $brush.Dispose()
    }

    # Draw N in the center taking ~70% of the size
    $pad = [math]::Round($size * 0.15)
    $drawSize = $size - 2 * $pad
    $g.DrawImage($orig, $pad, $pad, $drawSize, $drawSize)
    $g.Dispose()
    return $bmp
}

# Update all legacy mipmaps with SOLID BLACK background and the white glowing N in the center
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

    $iconSquare = Create-Icon $pureN $sz "black_square"
    $iconSquare.Save("$targetDir\ic_launcher.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $iconSquare.Dispose()

    $iconRound = Create-Icon $pureN $sz "black_round"
    $iconRound.Save("$targetDir\ic_launcher_round.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $iconRound.Dispose()

    Write-Host "Updated $folder ($sz x $sz) with black background"
}

$pureN.Dispose()
$src.Dispose()
Write-Host "All icons generated successfully!"
