package com.onestoptech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="toy_robot")
@JsonIgnoreProperties({"id"})
public class ToyRobot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden=true)
	private Long id;
	@Column(name="x")
	private int x;
	@Column(name="y")
	private int y;
	@Column(name="f")
	private String f;

	public ToyRobot() {}

	public ToyRobot(int x, int y, String f) {
		this.x = x;
		this.y = y;
		this.f = f;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getReport() {
		return "Output: " + this.x + "," + this.y  + "," + this.f;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ToyRobot toyRobot = (ToyRobot) o;
		return x == toyRobot.x &&
				y == toyRobot.y &&
				Objects.equal(f, toyRobot.f);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(x, y, f);
	}

	@Override
	public String toString() {
		return "ToyRobot{" +
				"id=" + id +
				", x=" + x +
				", y=" + y +
				", f='" + f + '\'' +
				'}';
	}
}