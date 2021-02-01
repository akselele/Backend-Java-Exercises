package be.vinci.pae.domain;

public class Text {

	private int id;
	private String content;
	private Level level;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLevel() {
		return level.toString();
	}

	public void setLevel(String level) {
		this.level = Level.valueOf(level.toUpperCase());
	}

	@Override
	public String toString() {
		return "TextImpl [id=" + id + ", content=" + content  + ", level=" + level +"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Text other = (Text) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
