Add-Type -AssemblyName System.Drawing

$srcPath = "C:\Users\Haris Laptops\.gemini\antigravity\brain\39b9a3d7-1c74-420f-80fa-ad1573a0de14\.user_uploaded\media_1795260711908_f3165464.jpg"
$src = [System.Drawing.Bitmap]::FromFile($srcPath)

Write-Host "Source dimensions: $($src.Width) x $($src.Height)"

# Let's find the exact bounding box of the N lettermark.
# The N lettermark is white/cyan. The background behind it is dark blue/teal.
# Let's sample colors:
# Center of N is around (512, 512).
Write-Host "Color at 512,512: $($src.GetPixel(512,512))"
Write-Host "Color at 300,512: $($src.GetPixel(300,512))"
Write-Host "Color at 700,512: $($src.GetPixel(700,512))"
Write-Host "Color at 512,300: $($src.GetPixel(512,300))"
Write-Host "Color at 512,700: $($src.GetPixel(512,700))"

# Check the background color inside the squircle (away from N):
# At (250, 250):
Write-Host "Color at 250,250 (dark blue squircle bg): $($src.GetPixel(250,250))"
# At (250, 750):
Write-Host "Color at 250,750 (teal squircle bg): $($src.GetPixel(250,750))"

$src.Dispose()
