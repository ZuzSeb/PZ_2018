package com.zuzseb.learning.model;

public class Pet {

	private String genera;
	private String name;
	private Integer weight;
	private String description;
	
	public Pet() {
		super();
	}

	public String getGenera() {
		return genera;
	}

	public void setGenera(String genera) {
		this.genera = genera;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((genera == null) ? 0 : genera.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pet other = (Pet) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (genera == null) {
			if (other.genera != null)
				return false;
		} else if (!genera.equals(other.genera))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pet [genera=");
		builder.append(genera);
		builder.append(", name=");
		builder.append(name);
		builder.append(", weight=");
		builder.append(weight);
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}

}
