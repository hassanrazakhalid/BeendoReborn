package com.Beendo.Utils;

public enum Role {

	ROOT_ADMIN {
		public String toString() {
			return "ROOT_ADMIN";
		}
	},

	ROOT_USER {
		public String toString() {
			return "ROOT_USER";
		}
	},
	ENTITY_ADMIN {
		public String toString() {
			return "ENTITY_ADMIN";
		}
	},

	ENTITY_USER {
		public String toString() {
			return "ENTITY_USER";
		}
	}

}
