package org.yetiz.lib.acd.Entity;

import java.util.Date;

/**
 * Created by yeti on 2015/4/16.
 */
public class FileInfo extends NodeInfo {
	protected String tempLink = "";
	protected AssetInfo[] assets = new AssetInfo[]{};
	protected ContentProperties contentProperties = null;

	public FileInfo() {
		kind = "FILE";
	}

	public String getTempLink() {
		return tempLink;
	}

	public ContentProperties getContentProperties() {
		return contentProperties;
	}

	public class ContentProperties {
		protected Long version = Long.MIN_VALUE;
		protected String md5 = "";
		protected Long size = Long.MIN_VALUE;
		protected String contentType = "";
		protected String extension = "";
		protected Date contentDate = new Date(0);
		protected Image image = null;
		protected Video video = null;

		public Long getVersion() {
			return version;
		}

		public String getMd5() {
			return md5;
		}

		public Long getSize() {
			return size;
		}

		public String getContentType() {
			return contentType;
		}

		public String getExtension() {
			return extension;
		}

		public Date getContentDate() {
			return contentDate;
		}

		public Image getImage() {
			return image;
		}

		public Video getVideo() {
			return video;
		}

		public class Image {
			protected Long height = Long.MIN_VALUE;
			protected Long width = Long.MIN_VALUE;
			protected String make = "";
			protected String model = "";
			protected Long iso = Long.MIN_VALUE;
			protected String exposureTime = "";
			protected String aperture = "";
			protected String dateTimeOriginal = "";
			protected String flash = "";
			protected String focalLength = "";
			protected String dateTime = "";
			protected String dateTimeDigitized = "";
			protected String software = "";
			protected String orientation = "";
			protected String colorSpace = "";
			protected String meteringMode = "";
			protected String exposureProgram = "";
			protected String exposureMode = "";
			protected String sharpness = "";
			protected String whiteBalance = "";
			protected String sensingMethod = "";
			protected Long xresolution = Long.MIN_VALUE;
			protected Long yresolution = Long.MIN_VALUE;
			protected String resolutionUnit = "";
			protected String gpsTimeStamp = "";
			protected String location = "";

			public Long getHeight() {
				return height;
			}

			public Long getWidth() {
				return width;
			}

			public String getMake() {
				return make;
			}

			public String getModel() {
				return model;
			}

			public Long getIso() {
				return iso;
			}

			public String getExposureTime() {
				return exposureTime;
			}

			public String getAperture() {
				return aperture;
			}

			public String getDateTimeOriginal() {
				return dateTimeOriginal;
			}

			public String getFlash() {
				return flash;
			}

			public String getFocalLength() {
				return focalLength;
			}

			public String getDateTime() {
				return dateTime;
			}

			public String getDateTimeDigitized() {
				return dateTimeDigitized;
			}

			public String getSoftware() {
				return software;
			}

			public String getOrientation() {
				return orientation;
			}

			public String getColorSpace() {
				return colorSpace;
			}

			public String getMeteringMode() {
				return meteringMode;
			}

			public String getExposureProgram() {
				return exposureProgram;
			}

			public String getExposureMode() {
				return exposureMode;
			}

			public String getSharpness() {
				return sharpness;
			}

			public String getWhiteBalance() {
				return whiteBalance;
			}

			public String getSensingMethod() {
				return sensingMethod;
			}

			public Long getXresolution() {
				return xresolution;
			}

			public Long getYresolution() {
				return yresolution;
			}

			public String getResolutionUnit() {
				return resolutionUnit;
			}

			public String getGpsTimeStamp() {
				return gpsTimeStamp;
			}

			public String getLocation() {
				return location;
			}
		}

		public class Video {
			protected Long height = Long.MIN_VALUE;
			protected Long width = Long.MIN_VALUE;
			protected String creationDate = "";
			protected Double duration = Double.MIN_VALUE;
			protected String videoCodec = "";
			protected Long videoBitrate = Long.MIN_VALUE;
			protected Double videoFrameRate = Double.MIN_VALUE;
			protected String audioCodec = "";
			protected Long audioBitrate = Long.MIN_VALUE;
			protected Long audioSampleRate = Long.MIN_VALUE;
			protected Long audioChannels = Long.MIN_VALUE;
			protected String audioChannelLayout = "";
			protected Long bitrate = Long.MIN_VALUE;
			protected Double rotate = Double.MIN_VALUE;
			protected String location = "";
			protected String title = "";
			protected String make = "";
			protected String model = "";
			protected String encoder = "";

			public Long getHeight() {
				return height;
			}

			public Long getWidth() {
				return width;
			}

			public String getCreationDate() {
				return creationDate;
			}

			public Double getDuration() {
				return duration;
			}

			public String getVideoCodec() {
				return videoCodec;
			}

			public Long getVideoBitrate() {
				return videoBitrate;
			}

			public Double getVideoFrameRate() {
				return videoFrameRate;
			}

			public String getAudioCodec() {
				return audioCodec;
			}

			public Long getAudioBitrate() {
				return audioBitrate;
			}

			public Long getAudioSampleRate() {
				return audioSampleRate;
			}

			public Long getAudioChannels() {
				return audioChannels;
			}

			public String getAudioChannelLayout() {
				return audioChannelLayout;
			}

			public Long getBitrate() {
				return bitrate;
			}

			public Double getRotate() {
				return rotate;
			}

			public String getLocation() {
				return location;
			}

			public String getTitle() {
				return title;
			}

			public String getMake() {
				return make;
			}

			public String getModel() {
				return model;
			}

			public String getEncoder() {
				return encoder;
			}
		}
	}

}

