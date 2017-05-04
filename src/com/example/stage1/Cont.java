package com.example.stage1;

public class Cont {
String name,phno;
boolean selected=false;
public Cont(String phno, String name, boolean selected) {
	  super();
	  this.phno = phno;
	  this.name = name;
	  this.selected = selected;
	 }
public String getphno() {
	  return phno;
	 }
	 public void setphno(String phno) {
	  this.phno = phno;
	 }
	 public String getName() {
	  return name;
	 }
	 public void setName(String name) {
	  this.name = name;
	 }
	 
	 public boolean isSelected() {
	  return selected;
	 }
	 public void setSelected(boolean selected) {
	  this.selected = selected;
	 }
}

