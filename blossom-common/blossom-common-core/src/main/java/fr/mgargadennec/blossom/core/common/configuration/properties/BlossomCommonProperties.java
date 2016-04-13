package fr.mgargadennec.blossom.core.common.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="blossom.common")
public class BlossomCommonProperties {
	private BlossomCommonHateoasProperties hateoas = new BlossomCommonHateoasProperties();

	
	public BlossomCommonHateoasProperties getHateoas() {
		return hateoas;
	}


	public void setHateoas(BlossomCommonHateoasProperties hateoas) {
		this.hateoas = hateoas;
	}


	public static class BlossomCommonHateoasProperties{
		private BlossomCommonHateoasCurieProperties curie = new BlossomCommonHateoasCurieProperties();

		public BlossomCommonHateoasCurieProperties getCurie() {
			return curie;
		}

		public void setCurie(BlossomCommonHateoasCurieProperties curie) {
			this.curie = curie;
		}
	}
	
	public static class BlossomCommonHateoasCurieProperties{
			private String uri = "http://www.cardiweb.com/rels/{rel}";
			private String prefix = "app";
			
			public String getUri() {
				return uri;
			}
			public void setUri(String uri) {
				this.uri = uri;
			}
			public String getPrefix() {
				return prefix;
			}
			public void setPrefix(String prefix) {
				this.prefix = prefix;
			}
	}
}
