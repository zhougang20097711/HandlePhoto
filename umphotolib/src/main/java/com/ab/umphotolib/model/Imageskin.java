package com.ab.umphotolib.model;

import android.graphics.Bitmap;

/**
 * Created by AB051788 on 2017/1/10.
 */
public class Imageskin {


		/**
		 * 高亮对比度特效
		 */
		BrightContrastFilter contrastFx;

		GaussianBlurFilter gaussianBlurFx;
		private ImageHandler image = null;


		public Imageskin(Bitmap bmp) {
			image = new ImageHandler(bmp);
		}

		public Imageskin(Bitmap bmp, int nSigma, float nBrightness, float nContrast) {
			gaussianBlurFx = new GaussianBlurFilter(bmp);
			gaussianBlurFx.Sigma = nSigma;
			image = gaussianBlurFx.imageProcess(); // 柔化处理
			contrastFx = new BrightContrastFilter(image);
			contrastFx.BrightnessFactor = nBrightness;
			contrastFx.ContrastFactor = nContrast;

			image = contrastFx.imageProcess(); //  高亮对比度
		}

		public ImageHandler imageProcess() {
			int width = image.getWidth();
			int height = image.getHeight();
			ImageHandler clone = image.clone();
			int old_r, old_g, old_b, r, g, b;
			for (int x = 0; x < (width - 1); x++) {
				for (int y = 0; y < (height - 1); y++) {
					old_r = clone.getRComponent(x, y);
					old_g = clone.getGComponent(x, y);
					old_b = clone.getBComponent(x, y);

					r = 255 - (255 - old_r) * (255 - image.getRComponent(x, y)) / 255;
					g = 255 - (255 - old_g) * (255 - image.getGComponent(x, y)) / 255;
					b = 255 - (255 - old_b) * (255 - image.getBComponent(x, y)) / 255;
					image.setPixelColor(x, y, r, g, b);
				}
			}
			return image;
		}



	public class GaussianBlurFilter {

		int Padding = 3;

		// / <summary>
		// / The bluriness factor.
		// / Should be in the range [0, 40].
		// / </summary>
		public float Sigma = 0.75f;

		private ImageHandler image = null;

		public GaussianBlurFilter(Bitmap bmp) {
			image = new ImageHandler(bmp);
		}

		float[] ApplyBlur(float[] srcPixels, int width, int height) {
			float[] destPixels = new float[srcPixels.length];
			System.arraycopy(srcPixels, 0, destPixels, 0, srcPixels.length);

			int w = width + Padding * 2;
			int h = height + Padding * 2;

			// Calculate the coefficients
			float q = Sigma;
			float q2 = q * q;
			float q3 = q2 * q;

			float b0 = 1.57825f + 2.44413f * q + 1.4281f * q2 + 0.422205f * q3;
			float b1 = 2.44413f * q + 2.85619f * q2 + 1.26661f * q3;
			float b2 = -(1.4281f * q2 + 1.26661f * q3);
			float b3 = 0.422205f * q3;

			float b = 1.0f - ((b1 + b2 + b3) / b0);

			// Apply horizontal pass
			ApplyPass(destPixels, w, h, b0, b1, b2, b3, b);

			// Transpose the array
			float[] transposedPixels = new float[destPixels.length];
			Transpose(destPixels, transposedPixels, w, h);

			// Apply vertical pass
			ApplyPass(transposedPixels, h, w, b0, b1, b2, b3, b);

			// transpose back
			Transpose(transposedPixels, destPixels, h, w);

			return destPixels;
		}

		void ApplyPass(float[] pixels, int width, int height, float b0, float b1, float b2, float b3, float b) {
			float num = 1f / b0;
			int triplewidth = width * 3;
			for (int i = 0; i < height; i++) {
				int steplength = i * triplewidth;
				for (int j = steplength + 9; j < (steplength + triplewidth); j += 3) {
					pixels[j] = (b * pixels[j]) + ((((b1 * pixels[j - 3]) + (b2 * pixels[j - 6])) + (b3 * pixels[j - 9])) * num);
					pixels[j + 1] = (b * pixels[j + 1]) + ((((b1 * pixels[(j + 1) - 3]) + (b2 * pixels[(j + 1) - 6])) + (b3 * pixels[(j + 1) - 9])) * num);
					pixels[j + 2] = (b * pixels[j + 2]) + ((((b1 * pixels[(j + 2) - 3]) + (b2 * pixels[(j + 2) - 6])) + (b3 * pixels[(j + 2) - 9])) * num);
				}
				for (int k = ((steplength + triplewidth) - 9) - 3; k >= steplength; k -= 3) {
					pixels[k] = (b * pixels[k]) + ((((b1 * pixels[k + 3]) + (b2 * pixels[k + 6])) + (b3 * pixels[k + 9])) * num);
					pixels[k + 1] = (b * pixels[k + 1]) + ((((b1 * pixels[(k + 1) + 3]) + (b2 * pixels[(k + 1) + 6])) + (b3 * pixels[(k + 1) + 9])) * num);
					pixels[k + 2] = (b * pixels[k + 2]) + ((((b1 * pixels[(k + 2) + 3]) + (b2 * pixels[(k + 2) + 6])) + (b3 * pixels[(k + 2) + 9])) * num);
				}
			}
		}

		void Transpose(float[] input, float[] output, int width, int height) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int index = (j * height) * 3 + (i * 3);
					int pos = (i * width) * 3 + (j * 3);
					output[index] = input[pos];
					output[index + 1] = input[pos + 1];
					output[index + 2] = input[pos + 2];
				}
			}
		}

		float[] ConvertImageWithPadding(ImageHandler imageIn, int width, int height) {
			int newheight = height + Padding * 2;
			int newwidth = width + Padding * 2;
			float[] numArray = new float[(newheight * newwidth) * 3];
			int index = 0;
			int num = 0;
			for (int i = -3; num < newheight; i++) {
				int y = i;
				if (i < 0) {
					y = 0;
				} else if (i >= height) {
					y = height - 1;
				}
				int count = 0;
				int negpadding = -1 * Padding;
				while (count < newwidth) {
					int x = negpadding;
					if (negpadding < 0) {
						x = 0;
					} else if (negpadding >= width) {
						x = width - 1;
					}
					numArray[index] = imageIn.getRComponent(x, y) * 0.003921569f;
					numArray[index + 1] = imageIn.getGComponent(x, y) * 0.003921569f;
					numArray[index + 2] = imageIn.getBComponent(x, y) * 0.003921569f;

					count++;
					negpadding++;
					index += 3;
				}
				num++;
			}
			return numArray;
		}

		public ImageHandler imageProcess() {
			int width = image.getWidth();
			int height = image.getHeight();
			float[] imageArray = ConvertImageWithPadding(image, width, height);
			imageArray = ApplyBlur(imageArray, width, height);
			int newwidth = width + Padding * 2;
			for (int i = 0; i < height; i++) {
				int num = ((i + 3) * newwidth) + 3;
				for (int j = 0; j < width; j++) {
					int pos = (num + j) * 3;
					image.setPixelColor(j, i, (int) (imageArray[pos] * 255f), (int) (imageArray[pos + 1] * 255f), (int) (imageArray[pos + 2] * 255f));
				}
			}
			return image;
		}

	}

	/**
	 * 高亮对比度特效
	 *
	 * @author 亚瑟boy
	 */
	public class BrightContrastFilter {

		private ImageHandler image = null;

		public float BrightnessFactor = 0.25f;

		/**
		 * The contrast factor. Should be in the range [-1, 1]
		 */
		public float ContrastFactor = 0f;

		public BrightContrastFilter(Bitmap bmp) {
			image = new ImageHandler(bmp);
		}

		public BrightContrastFilter(ImageHandler image) {
			this.image = image;
		}

		/**
		 * 高亮对比度特效
		 */
		public ImageHandler imageProcess() {
			int width = image.getWidth();
			int height = image.getHeight();
			int r, g, b;
			// Convert to integer factors
			int bfi = (int) (BrightnessFactor * 255);
			float cf = 1f + ContrastFactor;
			cf *= cf;
			int cfi = (int) (cf * 32768) + 1;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					r = image.getRComponent(x, y);
					g = image.getGComponent(x, y);
					b = image.getBComponent(x, y);
					// Modify brightness (addition)
					if (bfi != 0) {
						// Add brightness
						int ri = r + bfi;
						int gi = g + bfi;
						int bi = b + bfi;
						// Clamp to byte boundaries
						r = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
						g = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
						b = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
					}
					// Modifiy contrast (multiplication)
					if (cfi != 32769) {
						// Transform to range [-128, 127]
						int ri = r - 128;
						int gi = g - 128;
						int bi = b - 128;

						// Multiply contrast factor
						ri = (ri * cfi) >> 15;
						gi = (gi * cfi) >> 15;
						bi = (bi * cfi) >> 15;

						// Transform back to range [0, 255]
						ri = ri + 128;
						gi = gi + 128;
						bi = bi + 128;

						// Clamp to byte boundaries
						r = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
						g = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
						b = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
					}
					image.setPixelColor(x, y, r, g, b);
				}
			}
			return image;
		}

	}
}
