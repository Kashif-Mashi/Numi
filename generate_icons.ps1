Add-Type -AssemblyName System.Drawing

$srcPath = "C:\Users\Haris Laptops\.gemini\antigravity\brain\39b9a3d7-1c74-420f-80fa-ad1573a0de14\.user_uploaded\media_1795260711908_f3165464.jpg"
$baseDir = "C:\Users\Haris Laptops\Documents\personal repository\numi"
$resDir  = "$baseDir\app\src\main\res"

$src = [System.Drawing.Bitmap]::FromFile($srcPath)

# 1. Crop squircle (752x752 at 136,136)
$cropRect = New-Object System.Drawing.Rectangle(136, 136, 752, 752)
$cropped = $src.Clone($cropRect, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)

# Helper: Resize
function Resize-Bitmap($orig, $w, $h) {
    $bmp = New-Object System.Drawing.Bitmap($w, $h, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $g.DrawImage($orig, 0, 0, $w, $h)
    $g.Dispose()
    return $bmp
}

# Helper: Round
function Round-Bitmap($orig, $size) {
    $bmp = New-Object System.Drawing.Bitmap($size, $size, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $path = New-Object System.Drawing.Drawing2D.GraphicsPath
    $path.AddEllipse(0, 0, $size, $size)
    $g.SetClip($path)
    $g.DrawImage($orig, 0, 0, $size, $size)
    $g.Dispose()
    return $bmp
}

# Save app_icon.png for splash screen (512x512)
$appIcon = Resize-Bitmap $cropped 512 512
$appIcon.Save("$resDir\drawable\app_icon.png", [System.Drawing.Imaging.ImageFormat]::Png)
$appIcon.Dispose()
Write-Host "Saved app_icon.png"

# Save numi_launcher_fg.png for adaptive icon (432x432 with 288x288 centered icon)
$fgBmp = New-Object System.Drawing.Bitmap(432, 432, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
$gFg = [System.Drawing.Graphics]::FromImage($fgBmp)
$gFg.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
$gFg.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
$gFg.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
$gFg.DrawImage($cropped, 72, 72, 288, 288)
$gFg.Dispose()
$fgBmp.Save("$resDir\drawable\numi_launcher_fg.png", [System.Drawing.Imaging.ImageFormat]::Png)
$fgBmp.Dispose()
Write-Host "Saved numi_launcher_fg.png"

# Mipmaps
$sizes = @{
    "mipmap-mdpi"    = 48
    "mipmap-hdpi"    = 72
    "mipmap-xhdpi"   = 96
    "mipmap-xxhdpi"  = 144
    "mipmap-xxxhdpi" = 192
}

foreach ($entry in $sizes.GetEnumerator()) {
    $folder = $entry.Key
    $sz     = $entry.Value
    $targetDir = "$resDir\$folder"

    $sq = Resize-Bitmap $cropped $sz $sz
    $sq.Save("$targetDir\ic_launcher.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $sq.Dispose()

    $rd = Round-Bitmap $cropped $sz
    $rd.Save("$targetDir\ic_launcher_round.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $rd.Dispose()

    Write-Host "Saved $folder ($sz x $sz)"
}

$cropped.Dispose()
$src.Dispose()
Write-Host "ALL ICONS GENERATED SUCCESSFULLY"
