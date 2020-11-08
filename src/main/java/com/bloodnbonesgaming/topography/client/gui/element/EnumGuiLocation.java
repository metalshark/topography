package com.bloodnbonesgaming.topography.client.gui.element;

public enum EnumGuiLocation {
	TOP_LEFT {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return 0;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return 0;
		}
	},
	TOP_CENTER {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return displayWidth / 2 - imageWidth / 2;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return 0;
		}
	},
	TOP_RIGHT {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return displayWidth - imageWidth;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return 0;
		}
	},
	LEFT {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return 0;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return displayHeight / 2 - imageHeight / 2;
		}
	},
	CENTER {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return displayWidth / 2 - imageWidth / 2;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return displayHeight / 2 - imageHeight / 2;
		}
	},
	RIGHT {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return displayWidth - imageWidth;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return displayHeight / 2 - imageHeight / 2;
		}
	},
	BOTTOM_LEFT {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return 0;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return displayHeight - imageHeight;
		}
	},
	BOTTOM_CENTER {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return displayWidth / 2 - imageWidth / 2;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return displayHeight - imageHeight;
		}
	},
	BOTTOM_RIGHT {
		@Override
		public int getX(int displayWidth, final int imageWidth) {
			return displayWidth - imageWidth;
		}

		@Override
		public int getY(int displayHeight, final int imageHeight) {
			return displayHeight - imageHeight;
		}
	};

	public abstract int getX(final int displayWidth, final int imageWidth);

	public abstract int getY(final int displayHeight, final int imageHeight);
}
