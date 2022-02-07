package helio.rest.model;

import java.io.Serializable;

import lombok.Builder;

@Builder
public class ServiceMapping implements Serializable {

	private static final long serialVersionUID = 3151706018063463035L;
	private String id;
	private String name;
	private String content;
	private MappingType type;
	
	
}
