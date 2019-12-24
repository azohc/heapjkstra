class Par<T, U> {
	T fst; U snd;
	public Par(T f, U s) {
		fst = f; snd = s;
	}
	@Override
	public String toString() {
		return "(" + fst + ", " + snd + ")";
	}
}