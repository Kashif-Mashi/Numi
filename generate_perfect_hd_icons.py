import os
import math
from PIL import Image, ImageFilter, ImageEnhance

src_path = r"C:\Users\Haris Laptops\.gemini\antigravity\brain\39b9a3d7-1c74-420f-80fa-ad1573a0de14\.user_uploaded\media_1795260711908_f3165464.jpg"
res_dir = r"C:\Users\Haris Laptops\Documents\personal repository\numi\app\src\main\res"

im = Image.open(src_path).convert("RGB")
w, h = im.size

# N center is at (512, 512).
# Bounding box of N bright lines is ~460x424.
# Let's crop a 520x520 square tightly centered around (512, 512):
crop_size = 520
left = 512 - crop_size // 2
top = 512 - crop_size // 2
crop = im.crop((left, top, left + crop_size, top + crop_size))

# We want to create:
# 1. An ultra-sharp RGBA master image where:
#    - The N glyph (white strokes, cyan neon glow, numeral 1) is preserved in full HD.
#    - The dark card background fades smoothly to 100% transparent.
#    - No jagged thresholding! Smooth sub-pixel alpha.
# 2. When placed on black, it renders flawlessly.

master = Image.new("RGBA", (crop_size, crop_size), (0, 0, 0, 0))

# Precompute pixels
crop_pixels = crop.load()
master_pixels = master.load()

for y in range(crop_size):
    for x in range(crop_size):
        r, g, b = crop_pixels[x, y]
        lum = 0.299 * r + 0.587 * g + 0.114 * b

        # Radial fade towards the outer edge of the 520x520 box
        # so any background color near the edges drops to 0
        nx = (x - crop_size / 2.0) / (crop_size / 2.0)
        ny = (y - crop_size / 2.0) / (crop_size / 2.0)
        dist = math.sqrt(nx * nx + ny * ny)
        
        # Vignette fade factor between radius 0.65 and 0.95
        if dist < 0.65:
            vf = 1.0
        elif dist > 0.95:
            vf = 0.0
        else:
            t = (0.95 - dist) / (0.95 - 0.65)
            vf = t * t * (3.0 - 2.0 * t)

        # In this crop, the dark card background has lum between 30 and 65.
        # N stroke has lum > 170.
        # Glow has lum between 65 and 170.
        # We want smooth alpha:
        if lum <= 45:
            alpha = 0.0
        elif lum >= 155:
            alpha = 1.0
        else:
            t = (lum - 45.0) / (155.0 - 45.0)
            alpha = t * t * (3.0 - 2.0 * t)

        alpha *= vf

        if alpha <= 0.0:
            master_pixels[x, y] = (0, 0, 0, 0)
        else:
            # Color enhancement on transparent:
            # Boost brightness slightly on partial alpha so edges glow cleanly on dark backgrounds
            # without getting darkened by background blending
            a_int = int(min(255, max(0, round(alpha * 255))))
            master_pixels[x, y] = (r, g, b, a_int)

# Sharpen slightly for extra crispness at high DPI
enhancer = ImageEnhance.Sharpness(master)
master_sharp = enhancer.enhance(1.3)

# Save master 1024x1024 transparent N for in-app drawable (Splash screen, Top Bar, About screen)
app_icon = Image.new("RGBA", (1024, 1024), (0, 0, 0, 0))
# Center master inside 1024x1024
paste_pos = ((1024 - crop_size) // 2, (1024 - crop_size) // 2)
app_icon.paste(master_sharp, paste_pos, master_sharp)
app_icon.save(os.path.join(res_dir, "drawable", "app_icon.png"), "PNG")
print("Saved 1024x1024 HD app_icon.png")

# Now generate Adaptive Icon Foregrounds:
# Total canvas is 108dp x 108dp. Safe zone is central 72dp.
# So the N should occupy ~66% of the 108dp canvas (i.e. scale = 0.66).
adaptive_densities = {
    "mipmap-mdpi": 108,
    "mipmap-hdpi": 162,
    "mipmap-xhdpi": 216,
    "mipmap-xxhdpi": 324,
    "mipmap-xxxhdpi": 432
}

for folder, total_size in adaptive_densities.items():
    target_dir = os.path.join(res_dir, folder)
    os.makedirs(target_dir, exist_ok=True)

    # 108dp canvas
    fg_canvas = Image.new("RGBA", (total_size, total_size), (0, 0, 0, 0))
    # N size: ~66% of total_size
    n_size = int(round(total_size * 0.68))
    # Resize master using high quality Lanczos resampling
    n_resized = master_sharp.resize((n_size, n_size), Image.Resampling.LANCZOS)
    offset = (total_size - n_size) // 2
    fg_canvas.paste(n_resized, (offset, offset), n_resized)

    fg_path = os.path.join(target_dir, "ic_launcher_foreground.png")
    fg_canvas.save(fg_path, "PNG")
    print(f"Generated {folder}/ic_launcher_foreground.png ({total_size}x{total_size})")

# Now generate Legacy Icons (for launchers that don't support adaptive icons):
# Black background (#000000) with centered HD N
legacy_densities = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192
}

for folder, total_size in legacy_densities.items():
    target_dir = os.path.join(res_dir, folder)

    # Square legacy icon (solid black background)
    square_icon = Image.new("RGBA", (total_size, total_size), (0, 0, 0, 255))
    n_size = int(round(total_size * 0.72))
    n_resized = master_sharp.resize((n_size, n_size), Image.Resampling.LANCZOS)
    offset = (total_size - n_size) // 2
    square_icon.paste(n_resized, (offset, offset), n_resized)
    square_icon.save(os.path.join(target_dir, "ic_launcher.png"), "PNG")

    # Round legacy icon (solid black circle)
    round_icon = Image.new("RGBA", (total_size, total_size), (0, 0, 0, 0))
    # Draw black circle
    from PIL import ImageDraw
    draw = ImageDraw.Draw(round_icon)
    draw.ellipse((0, 0, total_size - 1, total_size - 1), fill=(0, 0, 0, 255))
    round_icon.paste(n_resized, (offset, offset), n_resized)
    round_icon.save(os.path.join(target_dir, "ic_launcher_round.png"), "PNG")

    print(f"Generated {folder}/ic_launcher.png & ic_launcher_round.png ({total_size}x{total_size})")

print("All HD icons successfully created!")
