import "./App.css";

export default function Button({ children, onClick }) {
  return (
    <div>
      <button
        type="button"
        onClick={onClick}
        className={`flex w-full justify-center rounded-md px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 ${
          children === "Reseteaza"
            ? "bg-black"
            : "bg-indigo-600 hover:bg-indigo-500"
        }`}
      >
        {children}
      </button>
    </div>
  );
}
